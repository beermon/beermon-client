package com.shopify.android.beermon;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.shopify.android.beermon.api.APIClientFactory;
import com.shopify.android.beermon.api.BeermonClient;
import com.shopify.android.beermon.async.ListCallback;
import com.shopify.android.beermon.cache.JSONCache;
import com.shopify.android.beermon.models.Beer;
import com.shopify.android.beermon.models.Tap;
import com.shopify.android.beermon.services.BeerService;
import com.shopify.android.beermon.services.TapService;
import com.shopify.android.beermon.views.SlantedTextView;
import com.shopify.android.beermon.views.TapFragment;
import org.codegist.crest.CRest;

import java.util.Random;

import java.util.Arrays;
import java.util.List;

public class BeerMon extends Activity {
    /**
     * Called when the activity is first created.
     */

    Tap leftTap, rightTap;


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

        BeermonClient client = new BeermonClient(this);
        client.getTaps(new ListCallback<Tap>() {
            @Override
            public void execute(List<Tap> list) {
                leftTap = list.get(0);
                rightTap = list.get(1);
            }
        });
    }
}
