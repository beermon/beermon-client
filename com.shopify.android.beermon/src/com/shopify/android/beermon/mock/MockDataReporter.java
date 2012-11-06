package com.shopify.android.beermon.mock;

import android.content.Context;
import android.util.Log;
import com.shopify.android.beermon.api.BeermonClient;
import com.shopify.android.beermon.async.PostCallback;
import com.shopify.android.beermon.models.Measurement;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: csaunders
 * Date: 2012-11-05
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockDataReporter {
    private final Object mutex = new Object();
    Context context;
    Reporter reporter;

    public MockDataReporter(Context context) throws Exception {
        this.context = context;
        reporter = new Reporter();
    }

    public void beginReporting() {
        new Thread(reporter).start();
    }

    public void endReporting() {
        synchronized (mutex){
            reporter.stop();
        }
    }

    private class Reporter implements Runnable {
        boolean running = true;
        BeermonClient client;

        public Reporter(){
            client = new BeermonClient(null);
        }

        public void stop(){
            synchronized (mutex){
                running = false;
            }
        }

        @Override
        public void run() {
            while(running){
                Measurement m = new Measurement();
                m.sampledAt = Calendar.getInstance().getTime();
                m.kegId = 2;
                m.temperature = 22.0f;
                m.volume = 15.0f;

                client.addMeasurement(m.kegId, m, new PostCallback() {
                    @Override
                    public void execute(boolean success, Object result) {
                        Log.d("MockDataReporter/Callback", result.toString());
                    }
                });

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e){
                    // zero fucks
                }
            }
        }
    }
}
