/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.core;


/***
 * For cases where representaion may not be applicable.
 * The Default value of request representation.
 */
public final class EmptyRepresentation extends Representation{

    private  static  final EmptyRepresentation INSTANCE = new EmptyRepresentation();

    private EmptyRepresentation() {
        super();
    }

    public static EmptyRepresentation getInstance(){
        return INSTANCE;
    }

}
