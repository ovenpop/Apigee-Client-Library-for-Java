/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.api.rest;

import com.apigee.sdk.oauth.api.exception.RestCallException;
import com.apigee.sdk.oauth.impl.core.EmptyRepresentation;
import com.apigee.sdk.oauth.impl.core.Representation;
import com.apigee.sdk.oauth.impl.core.RestClient;
import com.apigee.sdk.oauth.impl.model.*;
import java.util.Map;

/***
 * <pre>
 * A simple abstraction to make a REST call to a resource.
 * As of now , provides the ability to set body,headers,parameters.
 * Clients need to provide HTTP Verb and relative resource URI to start
 * with.All calls are proxied through Apigee Gateway.
 * The Gateway Application is responsible for building the final target URI
 * as well as OAuth Mediation.
 * The user of the API makes a request using the application user's smartkey
 * obtained when the user is added to the application registered with Apigee.
 *
 * <b>Examples:</b>
 * Example 1: Making a POST request using query parameter in URL
 * <pre>
 *      RestCallResult result = new RestCall(Request.HttpVerb.POST,"{relative uri}")
 *                                      .withSmartKey({smartkey of app user})
 *                                      .toProvider({api provider})
 *                                      .usingApp({app name registered with Apigee})
 *                                      .invoke();
 * </pre>
 *
 * Example 2: Making the same POST request using body but using the correct Content-Type value.
 * <pre>
 *
 *     RestCallResult result = new RestCall(Request.HttpVerb.POST,"{relative uri}")
 *                                      .withSmartKey({smartkey of app user})
 *                                      .toProvider({api provider})
 *                                      .usingApp({app name registered with Apigee})
 *                                      .withBody("{param-name1=param-value1&param-name2=param-value2}")
 *                                      .withHeader("Content-Type","application/x-www-form-urlencoded")
 *                                      .invoke();
 *
 *
 * </pre>
 *
 * NOTE: As of now, requests can be made ONLY to providers currently supported for OAuth Mediation in the Gateway App.
 */
public class RestCall {

    /***
     * This is the template for the URI to make the target request to.
     * This URI is *NOT* the actual provider URI. All requests are made to Apigee Gateway.
     * see the properties file bundled.
     */
    private static final String PROVIDER_REQUEST_URL_TEMPLATE = "provider.request.url.template";

    /***
     * The request model which is built for this request.
     */
    private Request request = new Request();

    /***
     * API provider name
     */
    private String provider;

    /***
     * The application name registered with Apigee
     */
    private String appName;

    /***
     * The application user's smartkey , which is used to get the OAuth Credentials to build the Authorization header.
     */
    private String smartKey;

    /***
     * The  target w/o the host and scheme. The final target URI is built by the Gateway App by using the combination
     * of relativeURI and provider name in the path.
    */
    private String relativeURI;

    /***
     * @param verb - the HTTP verb
     * @param resourceRelativeURI - The  target URI w/o the host and scheme.
     */
    public RestCall(Request.HttpVerb verb,String resourceRelativeURI){
        request.setHttpVerb(verb);
        relativeURI = resourceRelativeURI;
    }

    /**
     * Populates the provider for this request and returns RestCall for chaining
     * @param provider
     * @return
     */
    public RestCall toProvider(String provider){
        this.provider = provider;
        return this;
    }

    /***
     * Populates the application name for this request and returns RestCall for chaining
     * @param appName
     * @return
     */
    public RestCall usingApp(String appName){
        this.appName = appName;
        return this;
    }

    /***
     * Populates the user's smartkey for this request and returns RestCall for chaining
     * @param smartKey
     * @return
     */
    public RestCall withSmartKey(String smartKey){
        this.smartKey = smartKey;
        return this;
    }

    /***
     * Sets the body content as request representation and returns RestCall for chaining
     * @param content
     * @return
     */
    public RestCall withBody(String content){
        if(request.getRepresentation() instanceof EmptyRepresentation){
            request.setRepresentation(new Representation());
        }
        request.getRepresentation().setContent(content);
        return this;
    }

    /***
     * Sets the HTTP headers for this request.Returns RestCall for chaining
     * @param headers
     * @return
     */
    public RestCall withHeaders(Map<String,String> headers){
        request.getHeaders().putAll(headers);
        return this;
    }

    /***
     * Sets the HTTP header for this request. Returns RestCall for chaining
     * @return
     */
    public RestCall withHeader(String name,String value){
        request.getHeaders().put(name,value);
        return this;
    }

    /***
     * Sets the query parameters for this request.Returns RestCall for chaining
     * @param params
     * @return
     */
    public RestCall withParams(Map<String,String> params){
        request.getParams().putAll(params);
        return this;
    }

    /***
     * Sets the query parameter for this request.Returns RestCall for chaining
     * @param name
     * @param value
     * @return
     */
    public RestCall withParam(String name,String value){
        request.getParams().put(name,value);
        return this;
    }

    /***
     * Makes a request to Gateway(Apigee). Apigee Gateway makes the final Provider Request
     * after doing the OAuth Mediation for the user.
     * It first gets the Gateway URI by filling in the template for placeholders and
     * invokes it with Request Model Object.
     * @return  - ApiInvocationResult , the result of REST call.
     */
    public RestCallResult invoke() throws RestCallException {
       try{
           String target = buildTargetURI();
           request.setApiEndPoint(target);
           return RestClient.getInstance().invoke(request);
       }
       catch(Exception exp){
           throw new RestCallException(exp);
       }
    }

    /***
     * Builds the Gateway request URI from a template
     * see the properties file.
     * @return
     */
    private String buildTargetURI() {
         String template = com.apigee.sdk.oauth.impl.util.OAuthApiConfigurationLookup.getPropertyValue(PROVIDER_REQUEST_URL_TEMPLATE);
         if(relativeURI.startsWith("/")){
             relativeURI = relativeURI.substring(1);
         }
         String target = String.format(template,appName,provider,relativeURI);
         if(target.contains("?")){
             target = target +"&smartkey="+smartKey;
         }
         else{
             target = target +"?smartkey="+smartKey;
         }
         return target;
    }


}
