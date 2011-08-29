/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.util;

import java.util.Map;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

/***
 * A utility for handling json - which is main response type for the APIs.
 */
public class JsonSupport {

    private static Gson gson = new Gson();

    public static String getJsonFromMap(Map<String,String> map){
	   return gson.toJson(map,new TypeToken<Map<String, String>>() {}.getType());
	}


    public static Map<String,String> getMapFromJsonString(String jsonString){
		Map<String,String> deserializedMap = gson.fromJson(jsonString, new TypeToken<Map<String, String>>() {}.getType());
		return deserializedMap;
	}
}
