/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.resource;


import com.apigee.sdk.oauth.api.resource.Resource;
import com.apigee.sdk.oauth.api.rest.RestCallResult;
import com.apigee.sdk.oauth.impl.core.RestClient;
import com.apigee.sdk.oauth.api.exception.*;
import com.apigee.sdk.oauth.impl.model.AppOwner;

/**
 * *
 * Ideally will be the parent resource to provide any partial implementations and support methods if required.
 * Clients are advised to inherit from this type rather than {@link Resource}
 *
 * @see Resource for Contract details.
 */
public abstract class AbstractResource<T extends  Resource<T>>  implements Resource<T> {

    /**
     * The name of the application registered by Apigee User in Apigee.
     */
    private String appName;

    /**
     * The owner of the Application - typically would be Apigee User.
     * A person with Apigee login credentials.
     */
    private AppOwner applicationOwner;

    /**
     * @param appName - the application name of the app created by the Application Owner/Application Developer
     * @return
     * @see Resource for details
     */
    @SuppressWarnings("unchecked")
    @Override
    public T forApp(String appName) {
        this.appName = appName;
        return (T) this;  // For chaining
    }

    /**
     * @param owner - the Owner of the application(Apigee User).
     * @param <T>
     * @return
     * @see Resource for details
     */
    @Override
    public  T belongingTo(AppOwner owner) {
        this.applicationOwner = owner;
        return (T) this;
    }


    /**
     * A callback to the resource implementation to perform any pre creation operation.
     * Any Exception will result in Resource not getting created.
     *
     * @throws Exception
     * @see #onError(Throwable, com.apigee.sdk.oauth.api.rest.RestCallResult) also
     */
    protected void beforeCreate() throws Exception {
    }


    /**
     * *
     * A post creation callback to allow any customization on top of resultApi for any reason
     * or to perform custom operation like logging etc.
     *
     * @param resultApi
     * @see #onError(Throwable, com.apigee.sdk.oauth.api.rest.RestCallResult) also
     */
    protected void afterCreate(RestCallResult resultApi) {
    }



    /**
     * Handles the resource creation. This method allows subclasses to do any processing before
     * and after the resource is actually created.
     * Any Exception thrown results in error callback - allowing the API user to take any appropriate
     * action.
     *
     * @return - the result object on Resource invocation.
     */
    @Override
    public  T create() throws ResourceException {
        RestCallResult resultApi = null;
        try {
            this.beforeCreate();
            resultApi = this.onCreate(); // Actual internal impl
            this.afterCreate(resultApi);
        } catch (Exception e) {
            if (resultApi != null) {
                ResourceException exception = new ResourceException(e);
                exception.setHttpResponseCode(resultApi.getHttpResponseCode());
                throw exception;
            }
        }
        return (T)this;
    }

    /**
     * A callback during the create call to perform the actual REST API invocation to create the resource.
     * NOTE: Internal to implementation. Users are required to call create method for resource creation.
     *
     * @return - the result object on Resource invocation.
     */
    protected abstract RestCallResult onCreate();

    /**
     * Error callback for the API user to take any action required.
     *
     * @param error
     * @param resultApi
     */
    protected void onError(Throwable error, RestCallResult resultApi) {
    }

    /**
     * To Allow the implementations to make any required REST calls (HTTP invocations)
     * NOTE: Internal to implementation.
     *
     * @return - the client to make requests to the target end points for resource handling.
     */
    protected RestClient getRestAPIClient() {
        return RestClient.getInstance();
    }

    public String getAppName() {
        return appName;
    }

    public AppOwner getApplicationOwner() {
        return applicationOwner;
    }
}
