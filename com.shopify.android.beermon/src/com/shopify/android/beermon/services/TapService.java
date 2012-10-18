package com.shopify.android.beermon.services;

import com.shopify.android.beermon.models.Keg;
import com.shopify.android.beermon.models.Tap;
import org.codegist.crest.annotate.*;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-10-17
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 */
@EndPoint("http://beermon.herokuapp.com")
@Path("/taps")
@Consumes("application/json")
public interface TapService {

    // get taps
    @GET
    @Path(".json")
    Tap[] all();

    // get a tap
    @GET
    @Path("/{id}.json")
    Tap find(@PathParam("id") int id);

    // put a keg on a tap

    @PUT
    @Path("/{id}.json")
    void attachKegToTap(
            @PathParam("id") int id,
            @QueryParam("keg_id") int kegId
    );
}
