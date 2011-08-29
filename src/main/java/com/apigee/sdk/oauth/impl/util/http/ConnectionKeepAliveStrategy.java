/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.util.http;

import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.*;
import org.apache.http.protocol.*;

class ConnectionKeepAliveStrategy extends DefaultConnectionKeepAliveStrategy {

    private HttpConfigurationParams configurationParams;

    public ConnectionKeepAliveStrategy(HttpConfigurationParams configurationParams) {
        this.configurationParams = configurationParams;
    }

    @Override
    public long getKeepAliveDuration(HttpResponse response,
                                     HttpContext context) {
        long retVal = super.getKeepAliveDuration(response, context);
        if (retVal == -1 && configurationParams.connectionKeepAliveTimeoutSecs != -1) {
            retVal = configurationParams.connectionKeepAliveTimeoutSecs * 1000L;
        }
        return retVal;
    }
}