package com.shopify.android.beermon.models;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-10-17
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Keg {
    public String kind;
    public float remaining;
    public float capacity;
    @JsonProperty("created_at")
    public Date createdAt;
    @JsonProperty("updated_at")
    public Date updatedAt;
    @JsonProperty("beer_tap_id")
    public int beerTapId;
    @JsonProperty("beer_id")
    public int beerId;
    public Beer beer;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public float getRemaining() {
        return remaining;
    }

    public void setRemaining(float remaining) {
        this.remaining = remaining;
    }

    public float getCapacity() {
        return capacity;
    }

    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getBeerTapId() {
        return beerTapId;
    }

    public void setBeerTapId(int beerTapId) {
        this.beerTapId = beerTapId;
    }

    public int getBeerId() {
        return beerId;
    }

    public void setBeerId(int beerId) {
        this.beerId = beerId;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }
}
