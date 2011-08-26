
/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.sampleexamples;

import com.apigee.sdk.oauth.api.exception.RestCallException;
import com.apigee.sdk.oauth.api.rest.RestCallResult;
import com.apigee.sdk.oauth.api.rest.RestCall;
import com.apigee.sdk.oauth.impl.model.Request;
import com.apigee.sdk.oauth.impl.model.Request.*;

public class GetTwitterTrends {

   public static void main(String[] args){

       try {
           RestCallResult result = new RestCall(Request.HttpVerb.GET,"/1/trends.json")
             .withSmartKey("b5b340bd-9654-46ca-a467-c1bcbcc399af")
             .toProvider("twitter")
             .usingApp("aps")
             .invoke();
			 System.out.println(result.getResponsePayload());
       } catch (RestCallException e) {
		   System.out.println("Test failed");
		   e.printStackTrace();
       }
   }
}