/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.model;

/***
 * The model for the owner of the Application registered with Apigee.
 * The owner is an Apigee User.
 */
public class AppOwner {

    private final String ownerName;
    private final String ownerPassword;

    public AppOwner(String ownerName, String ownerPassword) {
        this.ownerName = ownerName;
        this.ownerPassword = ownerPassword;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerPassword() {
        return ownerPassword;
    }
}
