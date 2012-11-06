package com.shopify.android.beermon.models;

import com.shopify.android.beermon.serializers.APIObjectSerializer;
import org.codegist.crest.config.ParamType;
import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.io.Request;

import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-11-05
 * Time: 7:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class APIObjectWriter implements EntityWriter {
    private static final String CONTENT_TYPE = "application/json";
    private final APIObjectSerializer serializer;

    public APIObjectWriter() {
        serializer = new APIObjectSerializer();
    }

    @Override
    public void writeTo(Request request, OutputStream outputStream) throws Exception {
        serializer.serialize(request.getParams(ParamType.FORM), request.getMethodConfig().getCharset(), outputStream);
    }

    @Override
    public String getContentType(Request request) {
        return CONTENT_TYPE;
    }

    @Override
    public int getContentLength(Request request) {
        return -1;
    }
}
