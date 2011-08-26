/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class OAuthApiConfigurationLookup {

    private static final String POLICYCONFIG_PROPERTIES = "oauthApiConfig.properties";

    private static Properties configuration;

    static {
        try {
            configuration = loadPolicyConfigProperties();
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static Properties loadPolicyConfigProperties() throws IOException {
        InputStream stream = OAuthApiConfigurationLookup.class.getResourceAsStream("/" + OAuthApiConfigurationLookup.class.getPackage().getName().replace(".", "/") + "/" + POLICYCONFIG_PROPERTIES);
        Properties props = new Properties();
        props.load(stream);
        return props;
    }

    public static String getPropertyValue(String property) {
        Object prop = configuration.get(property);
        if (null == prop) return null;
        return prop.toString();
    }

    public static Properties getAllProperties(){
        return configuration;
    }

}

