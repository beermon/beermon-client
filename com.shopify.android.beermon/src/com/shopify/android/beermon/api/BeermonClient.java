package com.shopify.android.beermon.api;

import android.app.Activity;
import com.shopify.android.beermon.async.ListCallback;
import com.shopify.android.beermon.async.PostCallback;
import com.shopify.android.beermon.models.Beer;
import com.shopify.android.beermon.models.Keg;
import com.shopify.android.beermon.models.Measurement;
import com.shopify.android.beermon.models.Tap;
import com.shopify.android.beermon.services.APIService;
import com.shopify.android.beermon.services.BeerService;
import com.shopify.android.beermon.services.KegService;
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
    CRest crest;

    public BeermonClient(Activity activity){
        this.activity = activity;
        this.crest = APIClientFactory.getClient();
    }

    public void getTaps(final ListCallback<Tap> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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

    public void getBeers(final ListCallback<Beer> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BeerService service = crest.build(BeerService.class);

                final Beer[] beers = service.all();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.execute(Arrays.asList(beers));
                    }
                });
            }
        }).start();
    }

    public void createKeg(final int beerId, final Keg keg, final PostCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BeerService service = crest.build(BeerService.class);
                    final Keg responseKeg = service.createKeg(beerId, keg);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.execute(true, responseKeg);
                        }
                    });
                } catch (Exception e) {
                    callback.execute(false, e);
                }
            }
        }).start();
    }

    public void attachKeg(final int tapId, final int kegId, final PostCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TapService service = crest.build(TapService.class);
                    service.attachKegToTap(tapId, kegId);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.execute(true, null);
                        }
                    });
                } catch (Exception e) {
                    callback.execute(false, e);
                }
            }
        }).start();
    }

    public void addMeasurement(final int kegId, final Measurement measurement, final PostCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    KegService service = crest.build(KegService.class);
                    service.addMeasurementForKeg(kegId, measurement);
                } catch (Exception e) {
                    callback.execute(false, e);
                }
            }
        }).start();
    }
}
