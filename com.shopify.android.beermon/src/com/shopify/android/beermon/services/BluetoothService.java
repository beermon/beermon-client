package com.shopify.android.beermon.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.shopify.android.beermon.BluetoothServiceInterface;
import com.shopify.android.beermon.MainActivity;
import com.shopify.android.beermon.api.BeermonClient;
import com.shopify.android.beermon.async.ListCallback;
import com.shopify.android.beermon.async.PostCallback;
import com.shopify.android.beermon.cache.JSONCache;
import com.shopify.android.beermon.models.BluetoothMeasurement;
import com.shopify.android.beermon.models.Measurement;
import com.shopify.android.beermon.models.Tap;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: carson
 * Date: 2012-10-19
 * Time: 11:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class BluetoothService extends Service {

    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    static int alreadyRunningServices = 0;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    private long lastRefreshTime = 0;

    private static BluetoothService sharedInstance = null;

    // The onBind() method in the service class:
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    private final BluetoothServiceInterface.Stub mBinder = new BluetoothServiceInterface.Stub() {
        public void resetLeftTap() {

        }

        public void resetRightTap() {

        }
    };

    public void onCreate() {
        super.onCreate();

        alreadyRunningServices++;
        if (alreadyRunningServices > 1) {
            //quit
            stopSelf();
        }

        Log.v("Beermon", "Bluetooth service created");

        //WHOAAAA TEST CODE
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5000);
//
//                    BluetoothMeasurement measurement = new BluetoothMeasurement();
//                    measurement.temperature = 8;
//                    measurement.leftTapVolume = 1;
//                    measurement.rightTapVolume = 1;
//                    updateServer(measurement);
//
//                    Thread.sleep(2000);
//
//                    measurement.leftTapVolume += 2;
//                    measurement.rightTapVolume += 1;
//                    updateServer(measurement);
//
//                    Thread.sleep(2000);
//
//                    measurement.leftTapVolume += 1;
//                    measurement.rightTapVolume += 2;
//                    updateServer(measurement);
//
//                    Thread.sleep(2000);
//
//                    measurement.leftTapVolume += 5;
//                    measurement.rightTapVolume += 7;
//                    updateServer(measurement);
//                } catch (Exception e) {
//                    Log.e("Beermon", "Your testing of measurements messed up. " + e);
//                }
//            }
//        }).start();

        adapter = BluetoothAdapter.getDefaultAdapter();

        if(!adapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //get the bluetooth enabled, but then we're screwed because the activity will finish?
            enableBluetooth.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(enableBluetooth);
        }

        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice pairedDevice : pairedDevices)
            {
                if(pairedDevice.getName().equals("Beermon")) {
                    device = pairedDevice;
                    break;
                }
            }
        }

        if (device != null) {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
            try {
                socket = device.createRfcommSocketToServiceRecord(uuid);
                socket.connect();
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                beginListenForData();
            } catch (Exception exception) {
                Log.e("Beermon", "Error setting up bluetooth: " + exception.toString());
            }
        }
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = inputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            inputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            //check if data contains a measurement and parse it up
                                            if (data.startsWith("READING ")) {
                                                String[] splitData = data.split(" ");
                                                BluetoothMeasurement bluetoothMeasurement = new BluetoothMeasurement();
                                                bluetoothMeasurement.temperature = Float.parseFloat(splitData[1]);
                                                bluetoothMeasurement.rightTapVolume = Float.parseFloat(splitData[2]);
                                                bluetoothMeasurement.leftTapVolume = Float.parseFloat(splitData[3]);
                                                updateServer(bluetoothMeasurement);
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (Exception exception)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    private void updateServer(BluetoothMeasurement bluetoothMeasurement) {
        //only if it's been a minute
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRefreshTime >= 60000) {
            lastRefreshTime = currentTime;

            Log.v("Beermon", "Updated measurement: " + bluetoothMeasurement.toString());
            try {
                ArrayList<Tap> taps = JSONCache.load(this).getTaps();
                Measurement leftMeasurement = new Measurement();
                leftMeasurement.temperature = bluetoothMeasurement.temperature;
                leftMeasurement.kegId = taps.get(0).keg.id;
                leftMeasurement.sampledAt = new Date(System.currentTimeMillis());
                leftMeasurement.volume = bluetoothMeasurement.leftTapVolume;

                Measurement rightMeasurement = new Measurement();
                rightMeasurement.temperature = bluetoothMeasurement.temperature;
                rightMeasurement.kegId = taps.get(1).keg.id;
                rightMeasurement.sampledAt = new Date(System.currentTimeMillis());
                rightMeasurement.volume = bluetoothMeasurement.rightTapVolume;

                BeermonClient client = new BeermonClient(null);
                client.addMeasurement(leftMeasurement.kegId, leftMeasurement, new PostCallback() {
                    public void execute(boolean success, Object result) {
                        sendBroadcast(new Intent(MainActivity.REFRESH_DATA));
                    }
                });

                client.addMeasurement(rightMeasurement.kegId, rightMeasurement, new PostCallback() {
                    public void execute(boolean success, Object result) {
                        sendBroadcast(new Intent(MainActivity.REFRESH_DATA));
                    }
                });

            } catch (Exception exception) {
                Log.v("Beermon", "Exception: " + exception);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alreadyRunningServices--;

        stopWorker = true;

        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (Exception exception) {
            Log.e("Beermon", "Error tearing down bluetooth: " + exception.toString());
        }
    }
}
