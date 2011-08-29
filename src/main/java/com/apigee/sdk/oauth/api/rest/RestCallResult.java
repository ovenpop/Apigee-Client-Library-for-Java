/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.api.rest;


/**
 * *
 * Represents the result of REST API call. Captures the essential information like
 * Response code, response content,any exception.
 */
public class RestCallResult {

    /**
     * Captures the state of request - Set to ERROR in case of an Exception.
     * Clients however may need to check the httpResponseCode value to check
     * for any other error which might have occurred.
     */
    public enum ResultState {
        SUCCESS,
        ERROR
    }

    /**
     * Any Exception which might have occurred during the request execution.
     */
    private Throwable exception;

    /**
     * Indicates if request resulted in an Exception or otherwise.
     */
    private ResultState resultState;

    /**
     * Stores the value of response code from target.
     */
    private int httpResponseCode = 0;

    /**
     * Indicates the associated textual phrase for the response code
     */
    private String reasonPhrase = "";

    /**
     * Any response content for the request.
     */
    private String responsePayload = "";

    /**
     * Sets the response content from the response byte array.
     *
     * @param data
     */
    public void setResponsePayload(byte[] data) {
        if (null != data) {
            responsePayload = new String(data);
        }
    }

    /**
     * Returns the response content for this request as a String.
     *
     * @return
     */
    public String getResponsePayload() {
        return responsePayload;
    }

    /**
     * Returns the state of the request
     *
     * @return
     * @see {@code ResultState}
     */
    public ResultState getResultState() {
        return resultState;
    }

    /**
     * Sets the state of the request
     *
     * @see {@code ResultState}
     */
    public void setResultState(ResultState resultState) {
        this.resultState = resultState;
    }

    /**
     * Returns the http response code for the request.
     *
     * @return
     */
    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    /**
     * Sets the http response code for the request.
     *
     * @param httpResponseCode
     */
    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    /**
     * Returns the text associated with the http response code for the request.
     *
     * @return
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /**
     * Sets the text associated with the http response code for the request.
     */
    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * Returns any exception which might have occurred during the execution of this request
     *
     * @return
     */
    public Throwable getException() {
        return exception;
    }


    /**
     * Sets any exception which might have occurred during the execution of this request
     */
    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
