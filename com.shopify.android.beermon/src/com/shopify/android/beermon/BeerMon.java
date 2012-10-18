package com.shopify.android.beermon;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import com.shopify.android.beermon.api.APIClientFactory;
import com.shopify.android.beermon.models.Beer;
import com.shopify.android.beermon.services.BeerService;
import org.codegist.crest.CRest;
import org.codegist.crest.CRestException;

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

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CRest crest = APIClientFactory.getClient();

                Message msg = new Message();
                BeerService beerService = crest.build(BeerService.class);
                try {
                    Beer[] beers = beerService.all();

                    msg.obj = String.format("There are %d beers available!", beers.length);

                } catch (CRestException e) {
                    Log.v("OH SHIIIIII", e.toString());
                    msg.obj = "Shit hit the fan, check the logs";
                }
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
