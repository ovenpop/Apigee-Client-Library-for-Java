/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.core;

import com.apigee.sdk.oauth.api.rest.RestCallResult;
import com.apigee.sdk.oauth.impl.model.Request;
import com.apigee.sdk.oauth.impl.util.http.HttpSupport;
import org.apache.http.*;
import org.apache.log4j.Logger;
import org.apache.http.entity.*;
import org.apache.http.util.*;

import java.util.Map;


/***
 *  A simple client to the Resource Invocation Classes to make a REST call.
 *  Offers a single method to make the Request .
 *  @see {code invoke}
 *  Populates the result Object with the necessary details before returning.
 *  NOTE: This is meant only for internal consumption.
 *
 *  Please see RestCall for making a Provider Request.
 */
public class RestClient {

    private Logger logger = Logger.getLogger(RestClient.class);
    /***
     * Create the singleton instance
     */
    private static final RestClient INSTANCE = new RestClient();


    private RestClient(){

    }

    /***
     * Returns the single RestClient instance to be used for API usage.
     * @return
     */
    public static RestClient getInstance(){
        return INSTANCE;
    }

    /***
     * Creates an instance of HTTPSupport to make the required HTTP Request.
     * Populates the result Object with the necessary details before returning.
     * In case of any Exception,sets the result state as ERROR and stores the Exception
     * for any further processing.
     * @param request
     * @return
     */
    public final RestCallResult invoke(Request request) {
        RestCallResult result = new RestCallResult();
        HttpResponse response = null;
        HttpSupport httpSupport = null;
        try {
            httpSupport = new HttpSupport();
            Map<String,String> metaData = request.getRepresentation().getMeta();
            request.getHeaders().putAll(metaData);
            response =  httpSupport.executeHttpMethod(request);
            StatusLine status = response.getStatusLine();
            result.setHttpResponseCode(status.getStatusCode());
            result.setReasonPhrase(status.getReasonPhrase());
            ProtocolVersion pv = response.getProtocolVersion();
            if (response.getEntity() != null)
                result.setResponsePayload(EntityUtils.toByteArray(response.getEntity()));
            if (response.getEntity() != null) response.getEntity().consumeContent();
            result.setResultState(RestCallResult.ResultState.SUCCESS);
        } catch (Exception e) {
            logger.error(e);
            result.setException(e);
            result.setResultState(RestCallResult.ResultState.ERROR);
        } finally {
            cleanupOnResponse(response, httpSupport);
        }
        return result;
    }

    /***
     * Cleans up any resources claimed by HTTPSupport
     * @param httpResponse
     * @param httpSupport
     */
     private void cleanupOnResponse(HttpResponse httpResponse, HttpSupport httpSupport) {
        try {
            if(httpResponse != null){
                HttpEntity resEntity = httpResponse.getEntity();
                if (resEntity != null) {
                    resEntity.consumeContent();
                }
            }
            httpSupport.getHttpClient().getConnectionManager().shutdown();
        } catch (Exception exp) {//ignore
            logger.error("Error while consuming content from response ", exp);
        }
    }




}
