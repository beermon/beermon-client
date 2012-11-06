package com.shopify.android.beermon.services;

import com.shopify.android.beermon.models.APIObjectWriter;
import com.shopify.android.beermon.models.Measurement;
import com.shopify.android.beermon.serializers.APIObjectSerializer;
import org.codegist.crest.annotate.*;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-10-17
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
@EndPoint("http://beermon.herokuapp.com")
@Path("")
@Consumes("application/json")
@EntityWriter(APIObjectWriter.class)
public interface KegService extends APIService {

    @GET
    @Path("/kegs/{id}/measurements.json")
    Measurement[] measurementsForKeg(@PathParam("id") int kegId);

    @POST
    @Path("/kegs/{id}/measurements.json")
    void addMeasurementForKeg(
            @PathParam("id") int kegId,

            @FormParam("measurement")
            Measurement measurement
    );
}
