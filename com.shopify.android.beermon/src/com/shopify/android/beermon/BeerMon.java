package com.shopify.android.beermon;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shopify.android.beermon.api.APIClientFactory;
import com.shopify.android.beermon.models.Beer;
import com.shopify.android.beermon.services.BeerService;
import com.shopify.android.beermon.views.SlantedTextView;
import com.shopify.android.beermon.views.TapFragment;
import org.codegist.crest.CRest;
import org.codegist.crest.CRestException;

import java.util.Random;

public class BeerMon extends Activity {
    /**
     * Called when the activity is first created.
     */

    Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        FragmentManager fragmentManager = getFragmentManager();
        TapFragment tapFragment1 = (TapFragment)fragmentManager.findFragmentById(R.id.tap_1);
        TapFragment tapFragment2 = (TapFragment)fragmentManager.findFragmentById(R.id.tap_2);

        SlantedTextView slantedTextView1 = (SlantedTextView)tapFragment1.getView().findViewById(R.id.date);
        Random random = new Random();
        slantedTextView1.angle = random.nextInt(6) - 3;

        SlantedTextView slantedTextView2 = (SlantedTextView)tapFragment2.getView().findViewById(R.id.date);
        slantedTextView1.angle = random.nextInt(6) - 3;

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
                    Log.e("OH SHIIIIII", e.toString());
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
