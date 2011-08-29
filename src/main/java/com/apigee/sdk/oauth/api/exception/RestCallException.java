/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.api.exception;

public class RestCallException extends Exception{

    public RestCallException(Exception cause){
        super(cause);
    }

    public RestCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
