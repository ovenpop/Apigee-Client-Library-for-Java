package com.apigee.sdk.oauth.impl.core;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sun.font.CreatedFontTracker;
import sun.jvm.hotspot.utilities.Assert;

/**
 * Created by IntelliJ IDEA.
 * Author: Morgan
 * Date: 9/30/11
 * Time: 2:10 PM
 */
public class RepresentationTest {

    private Representation representation1;
    private Representation representation2;

    @BeforeMethod
    public void setUp() throws Exception {
        representation1 = new Representation();
        representation2 = new Representation();
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddMeta() throws Exception {

        // make sure adding data is working
        representation1.addMeta("foo", "bar");
        assert representation1.getMeta().containsKey("foo");
        assert representation1.getMeta().containsValue("bar");
        assert representation1.getMeta().get("foo").equals("bar");

        // make sure new representations aren't picking up other representations data.
        assert !representation2.getMeta().containsKey("foo");




        // Create a new representation with a different value for the same key.
        Representation representation3 = new Representation();
        representation3.addMeta("foo", "bang");

        assert representation3.getMeta().containsKey("foo");
        assert representation3.getMeta().containsValue("bang");
        assert representation3.getMeta().get("foo").equals("bang");
        assert !representation1.getMeta().get("foo").equals("bang");

    }

    @Test
    public void testGetMeta() throws Exception {

    }

    @Test
    public void testGetContent() throws Exception {

    }

    @Test
    public void testSetContent() throws Exception {

    }
}
