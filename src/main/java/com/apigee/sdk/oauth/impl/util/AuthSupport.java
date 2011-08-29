/*
 * Copyright (c) 2011, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 * 
 */
package com.apigee.sdk.oauth.impl.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EncodingUtils;

public class AuthSupport {

     public static String buildBasicAuthHeader(String user, String pwd) {
        StringBuilder tmp = new StringBuilder();
        tmp.append(user);
        tmp.append(":");
        tmp.append((pwd == null) ? "null" : pwd);
        byte[] base64password = Base64.encodeBase64(
                EncodingUtils.getBytes(tmp.toString(), HTTP.DEFAULT_PROTOCOL_CHARSET));

        CharArrayBuffer buffer = new CharArrayBuffer(32);
        buffer.append("Basic ");
        buffer.append(base64password, 0, base64password.length);
        return buffer.toString();
    }
}
