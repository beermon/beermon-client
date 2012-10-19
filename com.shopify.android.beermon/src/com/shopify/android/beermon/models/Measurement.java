package com.shopify.android.beermon.models;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.StringWriter;
import java.security.PublicKey;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-10-17
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Measurement {
    @JsonProperty("keg_id")
    public int kegId;
    @JsonProperty("created_at")
    public Date createdAt;
    @JsonProperty("updated_at")
    public Date updatedAt;
    public float volume;
    @JsonProperty("sampled_at")
    public Date sampledAt;
    public float temperature;

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "";
        }
    }
}
