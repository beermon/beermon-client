package com.shopify.android.beermon;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

public class BeerMon extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        String strScreenDIP = "";
        strScreenDIP += "The logical density of the display: " + dm.density + "\n";
        strScreenDIP += "The screen density expressed as dots-per-inch: " + dm.densityDpi +"\n";
        strScreenDIP += "The absolute height of the display in pixels: " + dm.heightPixels +"\n";
        strScreenDIP += "The absolute width of the display in pixels: " + dm.widthPixels + "\n";
        strScreenDIP += "A scaling factor for fonts displayed on the display: " + dm.scaledDensity + "\n";
        strScreenDIP += "The exact physical pixels per inch of the screen in the X dimension: " + dm.xdpi + "\n";
        strScreenDIP += "The exact physical pixels per inch of the screen in the Y dimension: " + dm.ydpi + "\n";

        Log.v("CDB", strScreenDIP);
    }
}
