package com.andrewslater.example;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MappingsTest {

    private static final String PATH = "/users/{userId}/address/{street}";

    @Test
    public void testComposePathWithMap() {
        Map<String, Object> props = new HashMap<>();
        props.put("userId", 229);
        props.put("street", "MainSt");

        Assert.assertEquals("/users/229/address/MainSt", Mappings.composePath(PATH, props));
    }

    @Test
    public void testComposePathWithVarargs() {
        Assert.assertEquals("/users/123/address/SouthSt", Mappings.composePath(PATH, 123, "SouthSt"));
    }
}
