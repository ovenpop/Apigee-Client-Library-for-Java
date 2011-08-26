
/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.sampleexamples;

import  com.apigee.sdk.oauth.api.exception.*;
import  com.apigee.sdk.oauth.api.resource.*;

public class CreateAppUser {

   public static void main(String[] args){

       try {
           AppUser user = AppUser.Factory.newInstance("sampleuser02","secret").forApp("aps").create();
		   System.out.println(user.getSmartKey());
       } catch (ResourceException e) {
		   System.out.println("Test failed");
		   e.printStackTrace();
       }
   }
}