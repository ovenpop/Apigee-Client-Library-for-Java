package com.apigee.sdk.oauth.impl.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.protocol.HTTP;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * Author: Morgan
 * Date: 9/30/11
 * Time: 3:18 PM
 */
public class AuthSupportTest {

    @Test
    public void testBuildBasicAuthHeader() throws Exception {
        String output;
        output = AuthSupport.buildBasicAuthHeader("user", "password");
        System.out.println(output + " ::: " + decodeAuthHeader(output));
    }

    @Test
    public void testBuildBasicAuthHeaderNullNull() throws Exception {
        String output;
        output = AuthSupport.buildBasicAuthHeader(null, null);
        System.out.println(output + " ::: " + decodeAuthHeader(output));

    }

    @Test
    public void testBuildBasicAuthHeaderValueNull() throws Exception {
        String output;
        output = AuthSupport.buildBasicAuthHeader("Foo", null);
        System.out.println(output + " ::: " + decodeAuthHeader(output));

    }

    @Test
    public void testBuildBasicAuthHeaderNullValue() throws Exception {
        String output;
        output = AuthSupport.buildBasicAuthHeader(null, "bar");
        System.out.println(output + " ::: " + decodeAuthHeader(output));

    }


    public String decodeAuthHeader(String header) throws Exception {
        String[] headerArray = header.split(" ");
        StringBuffer buffer = new StringBuffer();

        buffer.append(headerArray[0]);
        buffer.append(" ");
        buffer.append(new String(Base64.decodeBase64(headerArray[1].getBytes(HTTP.DEFAULT_PROTOCOL_CHARSET))));
        return buffer.toString();


    }

}
