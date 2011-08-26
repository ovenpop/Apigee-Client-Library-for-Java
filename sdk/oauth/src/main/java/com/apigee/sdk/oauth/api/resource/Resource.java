/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.api.resource;


import com.apigee.sdk.oauth.impl.core.Representation;
import com.apigee.sdk.oauth.api.exception.*;
import com.apigee.sdk.oauth.impl.model.AppOwner;


/**
 * <pre>
 * This represents the interface type of any resource created via Apigee REST API.
 * A resource can represent the following entities in the system:
 * a> Developer Application- the application created by an Apigee User for OAuth Mediation.
 * b> Application User - the End user of the Application.
 * c> Application Configuration - Represents any required configuration for application to be used against
 * a provider like twitter,salesforce etc. Typically these would be Client credentials as mandated by
 * OAuth spec.
 * The objective is to define the typical CRUD methods for dealing with Resource.
 *
 * <b>NOTE:</b>
 * Clients are advised to extends from {@link com.apigee.sdk.oauth.impl.resource.AbstractResource} rather then implement this interface directly.
 * </pre>
 * TODO: Add the remaining CRUD method support.
 */
public interface Resource {

    /**
     * The factory method to create the resource. Implementations are expected to make the actual
     * REST API call to create the entity.
     *
     * @see com.apigee.sdk.oauth.impl.resource.AbstractResource#create
     */
    <T extends Resource> T create() throws ResourceException;


    /**
     * *
     * The  method to set the application name to be used while creating the resource.
     *
     * @param appName - the application name of the app created by the Application Owner/Application Developer
     * @param <T>     - The type parameter to be instantiated with -to enforce a subtype of Resource
     * @return - The Resource instance for chaining.
     */
    <T extends Resource> T forApp(String appName);


    /**
     * *
     * The method to set the owner of the application- this would be the credentials of the Apigee User who
     * registered the application with Apigee.
     *
     * @param owner
     * @param <T>   - The type parameter to be instantiated with -to enforce a subtype of Resource
     * @return - The Resource instance for chaining.
     */
    <T extends Resource> T belongingTo(AppOwner owner);

    /**
     * *
     * A resource may have an input representation/body for it to be created. This allows implementing resource
     * handling in a generic fashion - for eg - in the actual request builder phase (which is an internal impl)
     *
     * @return Any input representation while making the actual request.
     *         <p/>
     *         NOTE: This is INTERNAL to the implementation.
     */
    Representation getInputRepresentation();


}
