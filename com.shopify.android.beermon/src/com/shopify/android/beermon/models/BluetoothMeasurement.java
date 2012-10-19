package com.shopify.android.beermon.models;

/**
 * Created with IntelliJ IDEA.
 * User: carson
 * Date: 2012-10-19
 * Time: 12:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class BluetoothMeasurement {
    public float temperature;

    public float leftTapVolume;

    public float rightTapVolume;

    public String toString() {
        return temperature + "°C, Left Tap: " + leftTapVolume + "L, Right Tap: " + rightTapVolume + "L";
    }
}
