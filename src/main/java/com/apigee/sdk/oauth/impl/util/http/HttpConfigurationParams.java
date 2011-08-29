/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.util.http;

import com.apigee.sdk.oauth.impl.util.OAuthApiConfigurationLookup;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.http.conn.params.*;
import org.apache.http.params.*;

public class HttpConfigurationParams {

    private Logger log = Logger.getLogger(HttpConfigurationParams.class);

    /**
     * Package Access for HTTPSupport Only.
     */

    int socketBufferSizeBytes = 8192;
    int maxTotalConnections = 100;
    int maxConnectionsPerRoute = 20;
    int connectionKeepAliveTimeoutSecs = 100;
    boolean staleConnectionCheckEnabled = false;
    int httpClientTimeout = 60 * 1000;

    private final String KEEP_ALIVE_TIMEOUT_SECS_PROP = "http.conn.keep-alive.timeout-secs";


    public void readProperties() {

        Properties props = OAuthApiConfigurationLookup.getAllProperties();

        maxTotalConnections = readInt(props,
                ConnManagerParams.MAX_TOTAL_CONNECTIONS,
                maxTotalConnections);
        maxConnectionsPerRoute = readInt(props,
                ConnManagerParams.MAX_CONNECTIONS_PER_ROUTE,
                maxConnectionsPerRoute);
        socketBufferSizeBytes = readInt(props,
                HttpConnectionParams.SOCKET_BUFFER_SIZE,
                socketBufferSizeBytes);
        connectionKeepAliveTimeoutSecs = readInt(props,
                KEEP_ALIVE_TIMEOUT_SECS_PROP,
                connectionKeepAliveTimeoutSecs);
        String staleConnectionCheckVal = props.getProperty(HttpConnectionParams.STALE_CONNECTION_CHECK);
        if (staleConnectionCheckVal != null) {
            staleConnectionCheckEnabled = Boolean.parseBoolean(staleConnectionCheckVal);
        }

    }

    private int readInt(Properties props, String key, int defaultVal) {
        String val = props.getProperty(key);
        int retValue = defaultVal;
        try {
            retValue = Integer.parseInt(val);
        } catch (Exception e) {
            log.warn("Failed to read propery " + key + "from val " + val
                    + " , using the default " + defaultVal);
        }
        return retValue;
    }
}
