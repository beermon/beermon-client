package com.shopify.android.beermon.serializers;

import android.util.Log;
import org.codegist.crest.param.Param;
import org.codegist.crest.serializer.Serializer;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Console;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-11-05
 * Time: 7:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class APIObjectSerializer implements Serializer<List<Param>> {
    private final ObjectMapper jackson;

    public APIObjectSerializer() {
        this.jackson = new ObjectMapper();
    }

    @Override
    public void serialize(List<Param> value, Charset charset, OutputStream outputStream) throws Exception {
        Log.d("APIObjectSerializer", "Serializing: " + value);
        JsonObject o = JsonObject.toJsonObject(value);
        jackson.writeValue(outputStream, o);
    }

    private static class JsonObject extends LinkedHashMap<String,Object> {

        static JsonObject toJsonObject(List<Param> value){
            JsonObject json = new JsonObject();
            for(Param p : value){
                String name = p.getParamConfig().getName();
                for(Object o : p.getValue()){
                    json.put(name, o);
                }
            }
            return json;
        }

        @Override
        public Object put(String key, Object value) {
            if(containsKey(key)) {
                Object o = get(key);
                Object prev = o;
                Collection<Object> dest;
                if(o instanceof Collection) {
                    dest = (Collection<Object>) o;
                }else{
                    dest = new ArrayList<Object>();
                    dest.add(o);
                    prev = super.put(key, dest);
                }
                dest.add(value);
                return prev;
            }else{
                return super.put(key, value);
            }
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> m) {
            for(Map.Entry<? extends String, ? extends Object> e : m.entrySet()){
                put(e.getKey(), e.getValue());
            }
        }
    }
}
