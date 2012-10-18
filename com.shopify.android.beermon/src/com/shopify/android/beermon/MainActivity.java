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

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    Tap leftTap, rightTap;

    TapFragment leftTapFragment, rightTapFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.

    }

    @Override
    protected void onResume() {
        super.onResume();

        FragmentManager fragmentManager = getFragmentManager();
        leftTapFragment = (TapFragment)fragmentManager.findFragmentById(R.id.tap_1);
        rightTapFragment = (TapFragment)fragmentManager.findFragmentById(R.id.tap_2);

        SlantedTextView slantedTextView1 = (SlantedTextView)leftTapFragment.getView().findViewById(R.id.date);
        Random random = new Random();
        slantedTextView1.angle = random.nextInt(6) - 3;

        SlantedTextView slantedTextView2 = (SlantedTextView)rightTapFragment.getView().findViewById(R.id.date);
        slantedTextView1.angle = random.nextInt(6) - 3;

        updateTaps();
    }


    private void updateTaps() {
        BeermonClient client = new BeermonClient(this);
        client.getTaps(new ListCallback<Tap>() {
            @Override
            public void execute(List<Tap> list) {
                leftTap = list.get(0);
                rightTap = list.get(1);

                leftTapFragment.update(leftTap);
                rightTapFragment.update(rightTap);
            }
        });
    }
}
