package com.shopify.android.beermon.api;

import android.app.Activity;
import com.shopify.android.beermon.async.ListCallback;
import com.shopify.android.beermon.models.Tap;
import com.shopify.android.beermon.services.TapService;
import org.codegist.crest.CRest;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-10-18
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class BeermonClient {
    private final Activity activity;

    public BeermonClient(Activity activity){
        this.activity = activity;
    }

    public void getTaps(final ListCallback<Tap> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CRest crest = APIClientFactory.getClient();
                TapService tapService = crest.build(TapService.class);

                final Tap[] taps = tapService.all();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.execute(Arrays.asList(taps));
                    }
                });

            }
        }).start();
    }
}
