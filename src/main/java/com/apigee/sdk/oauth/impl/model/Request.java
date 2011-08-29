/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.model;


import com.apigee.sdk.oauth.impl.core.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * A model for storing any data required to make the target HTTP request.
 * <p/>
 * NOTE: Meant for INTERNAL consumption. Users are advised to the use the appropriate APIs to act
 * on the resources or invoke target messages.
 * </pre>
 */
public class Request{

    /***
     * A type for the HTTP verb to be used for the request.
     */
    public static enum HttpVerb {
        GET("get"),
        POST("post"),
        PUT("put"),
        DELETE("delete");

        private String description;

        private HttpVerb(String desc){
           description = desc;
        }

        public String getDescription(){
            return description;
        }
    }

    /***
     * The httpVerb to use for the request
     */
    private HttpVerb httpVerb;

    /***
     * The target uri for the request
     */
    private String apiEndPoint;




    /***
     * The representation to be used for this request.
     * Initialised with a default value of an empty representation
     */
    private  Representation representation = EmptyRepresentation.getInstance();

    /***
     * Any request specific headers -
     * NOTE headers describing the request can also be stored in representation's metadata object.
     * In case of duplicate headers, representation's metadata will override the values here.
     */
    private  Map<String,String> headers = new ConcurrentHashMap<String, String>();

    /***
     * A simple representation of request parameters in the form of key-value pairs.
     */
    private  Map<String,String> params  = new ConcurrentHashMap<String, String>();



    /***
     * Convenience methods to add header and param to request
     */
    public void addHeader(String name,String value){
        headers.put(name,value);
    }

    public void addParam(String name,String value){
        params.put(name,value);
    }

    /***
     * Gets the value of Content-Type header.
     * First checks if the metadata of the representation. If not present,
     * checks the request headers.
     * @return
     */
    public String getContentTypeHeaderValue(){
        String contentType = representation.getMeta().get(Representation.MetaData.CONTENT_TYPE);
        if(null == contentType){
            contentType =  headers.get(Representation.MetaData.CONTENT_TYPE);
        }
        return contentType;
    }




    // ------------------------------------------
    /****
     * Getters and setter methods for fields
     * @return
     */
    // ------------------------------------------
    public Representation getRepresentation() {
        return representation;
    }

    public void setRepresentation(Representation representation) {
        this.representation = representation;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public HttpVerb getHttpVerb() {
        return httpVerb;
    }

    public void setHttpVerb(HttpVerb httpVerb) {
        this.httpVerb = httpVerb;
    }

    public String getApiEndPoint() {
        return apiEndPoint;
    }

    public void setApiEndPoint(String apiEndPoint) {
        this.apiEndPoint = apiEndPoint;
    }


}
