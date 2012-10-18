package com.shopify.android.beermon;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import com.shopify.android.beermon.api.APIClientFactory;
import com.shopify.android.beermon.cache.JSONCache;
import com.shopify.android.beermon.models.Beer;
import com.shopify.android.beermon.services.BeerService;
import org.codegist.crest.CRest;
import org.codegist.crest.CRestException;

import java.util.Arrays;
import java.util.List;

public class BeerMon extends Activity {
    /**
     * Called when the activity is first created.
     */

    Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView v = (TextView) findViewById(R.id.helloText);

        handler = new MyHandler(v);

        final Context ctx = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CRest crest = APIClientFactory.getClient();

                Message msg = new Message();
                BeerService beerService = crest.build(BeerService.class);
                Beer[] beers = beerService.all();

                JSONCache cache = new JSONCache();
                cache.beers.addAll(Arrays.asList(beers));

                try {
                    cache.save(ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                msg.obj = String.format("There are %d beers available!", beers.length);


                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    private class MyHandler extends Handler {
        TextView tv;
        public MyHandler(TextView tv) {
            this.tv = tv;
        }
        @Override
        public void handleMessage(Message message) {
            String msgObj = (String) message.obj;
            tv.setText(msgObj);
        }
    }
}
