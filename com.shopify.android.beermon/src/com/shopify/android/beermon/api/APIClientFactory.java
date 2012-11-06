package com.shopify.android.beermon.api;

import org.codegist.crest.CRest;
import org.codegist.crest.CRestBuilder;
import org.codegist.crest.CRestConfig;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-10-18
 * Time: 8:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class APIClientFactory {
    private static final String username = "05f8a4e34b57482ad021cfb0";
    private static final String password = "d84c1dfb42c2abcf0c90180e";

    public static CRest getClient() {
        CRestBuilder builder = new CRestBuilder();
        builder.basicAuth(username, password);
        builder.bindJsonDeserializerWith("application/json");
        return builder.build();
    }
}
