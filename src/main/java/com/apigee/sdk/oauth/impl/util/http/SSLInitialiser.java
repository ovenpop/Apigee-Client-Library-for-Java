/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.util.http;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.apache.http.conn.ssl.*;

public class SSLInitialiser {


    public  X509HostnameVerifier getHostNameVerifier() {
        return (X509HostnameVerifier) new X509HostnameVerifier() {

                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }

                @Override
                public void verify(String s, SSLSocket sslSocket) throws IOException {

                }

                @Override
                public void verify(String s, X509Certificate x509Certificate) throws SSLException {

                }

                @Override
                public void verify(String s, String[] strings, String[] strings1) throws SSLException {

                }
            };
    }

    public TrustManager getAcceptAllTrustManager() {
        return new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                    // I accept everything
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                    // I accept everything
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
    }
}
