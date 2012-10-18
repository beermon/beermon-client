package com.shopify.android.beermon.cache;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.shopify.android.beermon.models.Beer;
import com.shopify.android.beermon.models.Keg;
import com.shopify.android.beermon.models.Measurement;
import com.shopify.android.beermon.models.Tap;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-10-18
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSONCache {
    public static final String FILE_LOCATION = "cache.json";
    public ArrayList<Beer> beers;
    public ArrayList<Keg> kegs;
    public ArrayList<Measurement> measurements;
    public ArrayList<Tap> taps;

    public JSONCache() {
        beers = new ArrayList<Beer>();
        kegs = new ArrayList<Keg>();
        measurements = new ArrayList<Measurement>();
        taps = new ArrayList<Tap>();
    }

    public boolean save(Context context) throws IOException, JsonMappingException, JsonParseException {
        File file = getFile(context);
        if(externalStorageAvailable(true)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(getFile(context), this);
            return true;
        }
        return false;
    }

    public static JSONCache load(Context context) throws IOException, JsonMappingException, JsonParseException {
        JSONCache cache = new JSONCache();

        if(cache.externalStorageAvailable(false) && getFile(context).exists()) {
            ObjectMapper mapper = new ObjectMapper();
            File file = getFile(context);
            cache = mapper.readValue(getFile(context), JSONCache.class);
        }
        return cache;
    }

    private static File getFile(Context context) {
        return new File(context.getExternalFilesDir(null), FILE_LOCATION);
    }

    private boolean externalStorageAvailable(boolean requiresWrite) {
        boolean externalStorageAvailable = false;
        boolean externalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            externalStorageAvailable = externalStorageWriteable = true;
        } else if(requiresWrite && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            externalStorageAvailable = true;
            externalStorageWriteable = false;
        }

        return externalStorageAvailable && (externalStorageWriteable || !requiresWrite);
    }

    public ArrayList<Beer> getBeers() {
        return beers;
    }

    public void setBeers(ArrayList<Beer> beers) {
        this.beers = beers;
    }

    public ArrayList<Keg> getKegs() {
        return kegs;
    }

    public void setKegs(ArrayList<Keg> kegs) {
        this.kegs = kegs;
    }

    public ArrayList<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(ArrayList<Measurement> measurements) {
        this.measurements = measurements;
    }

    public ArrayList<Tap> getTaps() {
        return taps;
    }

    public void setTaps(ArrayList<Tap> taps) {
        this.taps = taps;
    }
}
