package com.apigee.sdk.oauth.impl.model;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * Author: Morgan
 * Date: 9/30/11
 * Time: 12:58 PM
 */
public class RequestTest {

    private Request request;

    @BeforeMethod
    public void setUp() throws Exception {
        request = new Request();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        request = null;
    }

    @Test
    public void testHttpVerb() throws Exception {
        assert Request.HttpVerb.DELETE.getDescription().equals("delete");
        assert Request.HttpVerb.GET.getDescription().equals("get");
        assert Request.HttpVerb.POST.getDescription().equals("post");
        assert Request.HttpVerb.PUT.getDescription().equals("put");
    }

    @Test
    public void testAddHeader() throws Exception {

    }

    @Test
    public void testAddParam() throws Exception {

    }

    @Test
    public void testGetContentTypeHeaderValue() throws Exception {

    }

    @Test
    public void testGetRepresentation() throws Exception {

    }

    @Test
    public void testSetRepresentation() throws Exception {

    }

    @Test
    public void testGetHeaders() throws Exception {

    }

    @Test
    public void testSetHeaders() throws Exception {

    }

    @Test
    public void testGetParams() throws Exception {

    }

    @Test
    public void testSetParams() throws Exception {

    }
}
