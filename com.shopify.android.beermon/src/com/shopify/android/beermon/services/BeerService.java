package com.shopify.android.beermon.services;

import com.shopify.android.beermon.models.Beer;
import com.shopify.android.beermon.models.Keg;
import org.codegist.crest.annotate.*;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-10-17
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
@EndPoint("http://beermon.herokuapp.com")
@Path("/beers")
@Consumes("application/json")
public interface BeerService {

    @GET
    @Path(".json")
    Beer[] all();

    @GET
    @Path("/{id}.json")
    Beer find(@PathParam("id") int id);

    @POST
    @Path("/{id}/kegs.json")
    Keg createKeg(
            @PathParam("id") int id,
            @QueryParam("keg") Keg keg
    );

}
