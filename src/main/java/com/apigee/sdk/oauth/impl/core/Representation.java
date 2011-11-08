/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * <pre>
 * This is used for denoting the state of resource along with some metadata like content-type etc.
 * Representation is modeled as a type here to allow generic handling of request .
 * For example,while creating a resource this type can be queried for body (intended state of the resource)
 * with any suitable headers- which comprise the metadata for resource.
 * </pre>
 *
 */
public class Representation {

    /***
     * A metadata singleton object.
     */
    private MetaData metaData = new MetaData();

    /***
     * The content of this Representation for a HTTP request to resource.
     * TODO : Is String correct type of representation. Will need to validate this.
     */
    private String content;


    public Representation(){
    }


    /***
     * The metadata associated with the representation.
     * Some typical examples include Media type,last modified etc.
    */
    public static class MetaData{

        public static final String CONTENT_TYPE="Content-Type";

        private Map<String,String> data = new ConcurrentHashMap<String, String>();
    }

    /***
     * To add any metadata name-value pair.
     * @param name - the name of Metadata
     * @param value - the value of Metadata
     */
    public void addMeta(String name,String value){
        metaData.data.put(name,value);
    }

    /***
     * Returns any metadata for this representation in the form of key-value pairs.
     * @return
     */
    public Map<String,String> getMeta(){
        return metaData.data;
    }

    /***
     * Returns any content of this Representation for a HTTP request to resource.
     */
    public String getContent() {
        return content;
    }

    /***
     * To set any content for the request.
     * TODO : Is String correct type of representation. Will need to validate this.
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
}
