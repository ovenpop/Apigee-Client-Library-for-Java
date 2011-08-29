/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.sampleexamples;

import com.apigee.sdk.oauth.api.resource.AppUser;

public class GetAuthURL {

    public static void main(String[] args) {
        String url = AppUser.Factory.newInstance(args[0],args[1]).forApp(args[2]).getAuthURL(args[3],args[4]);
        System.out.println(url);
    }
}
