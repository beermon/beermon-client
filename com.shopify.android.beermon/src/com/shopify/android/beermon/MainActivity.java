package com.shopify.android.beermon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import com.shopify.android.beermon.api.APIClientFactory;
import com.shopify.android.beermon.api.BeermonClient;
import com.shopify.android.beermon.async.ListCallback;
import com.shopify.android.beermon.cache.JSONCache;
import com.shopify.android.beermon.models.Beer;
import com.shopify.android.beermon.models.Tap;
import com.shopify.android.beermon.services.BeerService;
import com.shopify.android.beermon.services.BluetoothService;
import com.shopify.android.beermon.services.TapService;
import com.shopify.android.beermon.views.SlantedTextView;
import com.shopify.android.beermon.views.TapFragment;
import org.codegist.crest.CRest;

import java.util.*;

public class MainActivity extends Activity {

    public static String REFRESH_DATA = "com.shopify.android.beermon.action.refreshData";

    private class DataUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(REFRESH_DATA)) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastRefreshTime >= 60000) {
                    lastRefreshTime = currentTime;
                    Log.v("Beermon", "Broadcast to update taps received");
                    updateTaps();
                }
            }
        }
    }


    Tap leftTap, rightTap;

    TapFragment leftTapFragment, rightTapFragment;

    private DataUpdateReceiver dataUpdateReceiver;

    private long lastRefreshTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.

        startService(new Intent(getApplicationContext(), BluetoothService.class));
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

        if (dataUpdateReceiver == null) {
            dataUpdateReceiver = new DataUpdateReceiver();
        }
        IntentFilter intentFilter = new IntentFilter(REFRESH_DATA);
        registerReceiver(dataUpdateReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dataUpdateReceiver != null) {
            unregisterReceiver(dataUpdateReceiver);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                updateTaps();
                return true;
            case R.id.menu_about:
                // Build a dialog, design borrowed from Transdroid
                //TODO:
                AlertDialog.Builder changesDialog = new AlertDialog.Builder(this);
                changesDialog.setTitle("About Beermon");
                View changes = getLayoutInflater().inflate(R.layout.about, null);
                changesDialog.setView(changes);
                changesDialog.create();
                changesDialog.show();
                return true;
            case R.id.menu_change_keg:
                //TODO:
                return true;
        }
        return false;
    }

    private void updateTaps() {
        BeermonClient client = new BeermonClient(this);
        client.getTaps(new ListCallback<Tap>() {
            @Override
            public void execute(List<Tap> list) {
                JSONCache cache = new JSONCache();
                cache.setTaps(new ArrayList<Tap>(list));
                try {
                    cache.save(MainActivity.this);
                } catch (Exception exception) {
                    Log.e("Beermon", "Exception on saving new tap list: "+ exception);
                }
                leftTap = list.get(0);
                rightTap = list.get(1);

                leftTapFragment.update(leftTap);
                rightTapFragment.update(rightTap);
            }
        });
    }
}
