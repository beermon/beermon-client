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
public class Beer {

    public String name;
    public String brewery;
    public float abv;
    public float srm;
    public float ibus;
    @JsonProperty("created_at")
    public Date createdAt;
    @JsonProperty("updated_at")
    public Date updatedAt;
    public float rating;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrewery() {
        return brewery;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }

    public float getAbv() {
        return abv;
    }

    public void setAbv(float abv) {
        this.abv = abv;
    }

    public float getSrm() {
        return srm;
    }

    public void setSrm(float srm) {
        this.srm = srm;
    }

    public float getIbus() {
        return ibus;
    }

    public void setIbus(float ibus) {
        this.ibus = ibus;
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
}
