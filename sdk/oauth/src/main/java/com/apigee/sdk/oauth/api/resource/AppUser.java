/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.api.resource;

import com.apigee.sdk.oauth.api.exception.*;

/***
 * User facing interface type for Application user resource.
 * For example,Clients need to deal only with interface type to create
 * an application user.
 * Example usage:
 * <pre>
 *   1. <b>Creation of User:</b>
 *       AppUser user = AppUser.Factory.newInstance("<name>","<pwd>").forApp("<appname>").create();
 *   2. <b>Sign in:</b>
 *       AppUser user = AppUser.Factory.newInstance("<name>","<pwd>").forApp("<appname>").getSmartKey();
 * </pre>
 *
 * @see com.apigee.sdk.oauth.api.rest.RestCall for making a API request(eg: Twitter,Salesforce request)
 * user.getSmartKey();
 *
 * Smartkey can be used to make an OAuth Authorization request.
 */
public interface AppUser extends Resource {

    /***
     * Returns the user's smartkey.
     * The implementation is capable of getting the smartkey on demand if not present for this object.
     * @return
     */
    public String getSmartKey();

    /***
     * Overriden from base interface for correct chaining to work.
     * @return
     * @throws com.apigee.sdk.oauth.api.exception.ResourceException
     */
    @Override
    public AppUser create() throws ResourceException;


    /***
     * Overriden from base interface for correct chaining to work.
     * @param appName - the application name of the app created by the Application Owner/Application Developer
     * @return
     */
    @Override
    public AppUser forApp(String appName);


    public String getAuthURL(String provider,String callbackURL);


    /***
     * The factory type of App user to be used by the api user to get an internal implementation of the App user.
     */
    public static class Factory{

        public static AppUser newInstance(String userName, String password){
            return new com.apigee.sdk.oauth.impl.resource.AppUser(userName,password,userName);
        }
    }

}
