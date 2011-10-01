package com.apigee.sdk.oauth.impl.model;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * Author: Morgan
 * Date: 9/30/11
 * Time: 12:39 PM
 */
public class AppOwnerTest {

    private AppOwner appOwner = null;

    @BeforeMethod
    public void setUp() throws Exception {
        appOwner = new AppOwner("Name", "password");
    }

    @AfterMethod
    public void tearDown() throws Exception {
        appOwner = null;
    }

    @Test
    public void testGetOwnerName() throws Exception {
        assert appOwner.getOwnerName().equals("Name");
        assert appOwner.getOwnerName().equals("false") == false;
    }

    @Test
    public void testGetOwnerPassword() throws Exception {
        assert appOwner.getOwnerPassword().equals("password");
        assert appOwner.getOwnerPassword().equals("false") == false;

    }
}
