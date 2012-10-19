package com.shopify.android.beermon.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: carson
 * Date: 2012-10-19
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class StartupBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent();
        serviceIntent.setAction("com.shopify.android.beermon.services.BluetoothService");
        context.startService(serviceIntent);
    }
}
