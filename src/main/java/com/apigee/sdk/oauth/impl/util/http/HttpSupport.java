/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.util.http;


import com.apigee.sdk.oauth.impl.core.Representation;
import com.apigee.sdk.oauth.impl.model.Request;
import com.apigee.sdk.oauth.impl.util.Constants;
import org.apache.http.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;
import org.apache.http.params.*;
import org.apache.http.protocol.*;
import org.apache.http.conn.ssl.*;
import org.apache.http.entity.*;
import org.apache.http.conn.scheme.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.entity.*;
import org.apache.http.conn.params.*;
import org.apache.http.client.params.*;
import org.apache.http.conn.*;
import org.apache.http.impl.conn.tsccm.*;

import javax.net.ssl.*;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Wrapper around Apache HTTP Client
 */
public class HttpSupport {

    private DefaultHttpClient httpClient = null;
    private Logger logger = Logger.getLogger(HttpSupport.class);

    public DefaultHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Configuration params for Apache HTTP Client
     */
    private HttpConfigurationParams configurationParams = new HttpConfigurationParams();
    private SSLInitialiser sslInitialiser = new SSLInitialiser();


    public HttpSupport() throws Exception {
        configurationParams.readProperties();
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, configurationParams.maxTotalConnections);
        System.setProperty("http.conn-manager.max-per-route", String.valueOf(configurationParams.maxConnectionsPerRoute));
        SchemeRegistry schemeRegistry = initialiseSchemForHTTP();
        initialiseSchemeForHTTPS(schemeRegistry);
        HttpClientParams.setRedirecting(params, false);
        params.setBooleanParameter(HttpConnectionParams.STALE_CONNECTION_CHECK, configurationParams.staleConnectionCheckEnabled);
        params.setIntParameter(HttpConnectionParams.SOCKET_BUFFER_SIZE, configurationParams.socketBufferSizeBytes);
        HttpConnectionParams.setSoTimeout(params, configurationParams.httpClientTimeout);
        HttpConnectionParams.setConnectionTimeout(params, configurationParams.httpClientTimeout);
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        httpClient = new DefaultHttpClient(cm, params);
        httpClient.setKeepAliveStrategy(new ConnectionKeepAliveStrategy(configurationParams));
    }

    private SchemeRegistry initialiseSchemForHTTP() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        return schemeRegistry;
    }

    private void initialiseSchemeForHTTPS(SchemeRegistry schemeRegistry) throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager x509TrustManager = sslInitialiser.getAcceptAllTrustManager();
        X509HostnameVerifier hostnameVerifier = sslInitialiser.getHostNameVerifier();
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[]{x509TrustManager}, null);
        org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(sslcontext);
        socketFactory.setHostnameVerifier(hostnameVerifier);
        schemeRegistry.register(new Scheme("https", socketFactory, 443));
    }

    public HttpResponse executeHttpMethod(Request request) throws Exception {
        HttpRequestBase httpMethod;
        String apiEndPoint = addAnyParametersToRequestURI(request);
        apiEndPoint = encodeQueryParamsIfPresent(apiEndPoint);
        String requestHTTPVerb = request.getHttpVerb().getDescription();
        httpMethod = getHTTPMethod(requestHTTPVerb, apiEndPoint);
        applyTestSpecificHeaders(request, httpMethod);
        if ("POST".equalsIgnoreCase(requestHTTPVerb) || "PUT".equalsIgnoreCase(requestHTTPVerb)) {
            handleRequestWithPayload(request, httpMethod);
        }
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        try {
            HttpResponse httpResp = httpClient.execute(httpMethod);
            return httpResp;
        } catch (Exception exp) {
            logger.error(exp.getMessage(), exp);
            throw exp;
        }
    }


    private String addAnyParametersToRequestURI(Request request) {
         String url = request.getApiEndPoint();
        if (!(Constants.APPLICATION_X_WWW_FORM_URLENCODED.equalsIgnoreCase(request.getHeaders().get(Representation.MetaData.CONTENT_TYPE)))) {
             Map<String,String> params = request.getParams();
             Iterator<Map.Entry<String,String>> paramsIterator = params.entrySet().iterator();
             StringBuilder buffer = new StringBuilder();
             while(paramsIterator.hasNext()){
                 Map.Entry<String,String> entry = paramsIterator.next();
                 String param = entry.getKey();
                 String value = entry.getValue();
                 buffer.append(param+"="+value);
                 if(paramsIterator.hasNext()){
                    buffer.append("&");
                 }
             }
             if (buffer.toString().length() > 0) {
                url = url + ((url.indexOf("?") < 0) ? "?" : "&") + buffer.toString();
             }
         }
         return url;
    }


    /**
     * Applies any headers for the API sent by the user.
     *
     * @param options
     * @param httpMethod
     */
    private void applyTestSpecificHeaders(final Request request, HttpRequestBase httpMethod) {
        Map<String, String> headers = request.getHeaders();
        Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String hdrname = entry.getKey();
            String val = entry.getValue();
            Header header = new BasicHeader(hdrname, val);
            httpMethod.addHeader(header);
        }

    }


    private void handleRequestWithPayload(Request request, HttpRequestBase httpMethod) throws UnsupportedEncodingException {
        String content = request.getRepresentation().getContent();
        if (content != null) {
            StringEntity se = new StringEntity(content);
            ((HttpEntityEnclosingRequestBase) httpMethod).setEntity(se);
        } else {
            handleParametersForRequestWithPayload(request, httpMethod);
        }
    }


    /**
     * Any parameters entered during POST request are sent across in name/value form
     * to the API.
     */
    private void handleParametersForRequestWithPayload(final Request request, HttpRequestBase httpMethod) throws UnsupportedEncodingException {
        if (Constants.APPLICATION_X_WWW_FORM_URLENCODED.equalsIgnoreCase(request.getContentTypeHeaderValue())) {
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            Map<String, String> params = request.getParams();
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String paramName = entry.getKey();
                String paramValue = entry.getValue();
                formParams.add(new BasicNameValuePair(paramName, paramValue));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Constants.UTF_8);
            ((HttpEntityEnclosingRequestBase) httpMethod).setEntity(entity);
        }
    }


    public String encodeQueryParamsIfPresent(String proxyURL) {
        int index = proxyURL.indexOf('?');
        if (index != -1) {
            String qpString = proxyURL.substring(index + 1);
            if (!qpString.trim().equals("")) {
                String encodedQueryParamString = "";
                try {
                    encodedQueryParamString = EncodeUtil.encodeQuery(qpString, Constants.UTF_8);
                } catch (Exception e) {
                    logger.error("Failed to encode query params for URL " + proxyURL, e);
                }
                return proxyURL.substring(0, index) + "?" + encodedQueryParamString;
            }
        }
        return proxyURL;
    }

    private HttpRequestBase getHTTPMethod(String verb, String target) {
        HttpRequestBase httpMethod;
        if ("GET".equalsIgnoreCase(verb)) {
            httpMethod = new HttpGet(target);
        } else if ("POST".equalsIgnoreCase(verb)) {
            httpMethod = new HttpPost(target);
        } else if ("PUT".equalsIgnoreCase(verb)) {
            httpMethod = new HttpPut(target);
        } else {
            httpMethod = new HttpDelete(target);
        }
        return httpMethod;
    }


}
