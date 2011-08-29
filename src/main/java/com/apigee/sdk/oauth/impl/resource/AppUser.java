/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.resource;

import com.apigee.sdk.oauth.api.rest.RestCallResult;
import com.apigee.sdk.oauth.impl.core.Representation;
import com.apigee.sdk.oauth.impl.model.Request;
import com.apigee.sdk.oauth.impl.util.AuthSupport;
import com.apigee.sdk.oauth.impl.util.Constants;
import com.apigee.sdk.oauth.impl.util.JsonSupport;
import com.apigee.sdk.oauth.impl.util.OAuthApiConfigurationLookup;
import com.apigee.sdk.oauth.api.exception.*;

import java.util.HashMap;
import java.util.Map;

/***
 * Resource for the Application user.
 */
public class AppUser extends AbstractResource<com.apigee.sdk.oauth.api.resource.AppUser> implements com.apigee.sdk.oauth.api.resource.AppUser {

    /***
     * The name of the user(end user) to be used for the application concerned
     */
    private final String userName;

    /***
     * The password of the user(end user) to be used for the application concerned
     */
    private final String password;

    /***
     * The full name of the user(end user) to be used for the application concerned
     * Will default to the userName if not supplied.
     */
    private String fullName;

    /***
     * The user's smartkey
     */
    private String smartKey;


    public AppUser(String userName, String password) {
        this(userName,password,userName);
    }

    public AppUser(String userName, String password, String fullName) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
    }

    /***
     * The callback called during resource creation.
     * Responsible for creating the User Resource
     * @return - the result of user creation.
     */
    @Override
    protected RestCallResult onCreate() {
        Request request = new Request();
        request.setRepresentation(getInputRepresentation());
        request.setHttpVerb(Request.HttpVerb.POST);
        request.setApiEndPoint(getUserCreationApiEndPoint());
        RestCallResult result = getRestAPIClient().invoke(request);
        Map jsonMap = JsonSupport.getMapFromJsonString(result.getResponsePayload());
        if(jsonMap != null){
           this.smartKey = (String)jsonMap.get("smartKey");
        }
        return result;
    }

    private String getUserCreationApiEndPoint() {
        String template =  OAuthApiConfigurationLookup.getPropertyValue("application.user.resource.uri");
        return template.replace("{appName}",getAppName());
    }

    private String getUserSmartkeyApiEndPoint() {
        String template =  OAuthApiConfigurationLookup.getPropertyValue("application.user.smartkey.uri");
        return template.replace("{appName}",getAppName());
    }

    /***
     * Builds the representation for creating application user.
     * Constructs the body and sets the content-type for the request.
     * @return
     */
    @Override
    public Representation getInputRepresentation() {
         Representation representation = new Representation();
         Map<String,String> params = new HashMap<String, String>();
         params.put("userName",userName);
         params.put("fullName",fullName);
         params.put("password",password);
         String content = JsonSupport.getJsonFromMap(params);
         representation.setContent(content);
         representation.addMeta(Representation.MetaData.CONTENT_TYPE, Constants.APPLICATION_JSON);
         return representation;
    }




    /***
     * Makes a REST API call to get the smartkey for the application user
     * @return -app user's smartkey
     */
    @Override
    public String getSmartKey(){
        if(smartKey != null){
            return smartKey;
        }

        // Make the REST call to get the smartkey.
        Request request = new Request();
        request.setHttpVerb(Request.HttpVerb.GET);
        request.addHeader(Constants.AUTHORIZATION, AuthSupport.buildBasicAuthHeader(userName, password));
        request.setApiEndPoint(getUserSmartkeyApiEndPoint());
        RestCallResult result = getRestAPIClient().invoke(request);
        Map jsonMap = JsonSupport.getMapFromJsonString(result.getResponsePayload());
        if(jsonMap != null){
            smartKey = (String)jsonMap.get("smartKey");
            return smartKey;
        }
        else{
            return null;
        }
    }

  

    @Override
    public String getAuthURL(String provider, String callbackURL) {
         String template = OAuthApiConfigurationLookup.getPropertyValue("auth.url.template");
         return String.format(template,getAppName(),provider,getSmartKey(),callbackURL);
    }


    // ------------------------------------------
    /****
     * Getters and setter methods for fields
     * @return
     */
    // ------------------------------------------

}
