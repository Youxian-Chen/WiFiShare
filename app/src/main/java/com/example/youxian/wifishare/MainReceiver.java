package com.example.youxian.wifishare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


/**
 * Created by Youxian on 11/21/15.
 */
public class MainReceiver extends BroadcastReceiver {
    private static final String TAG = MainReceiver.class.getName();
    private MainActivity mActivity;
    private boolean isConnectToWifi = false;
    public MainReceiver() {
        super();
    }

    public MainReceiver(MainActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, action);
        if (MyHostApduService.WIFI_CONFIG.equals(action)) {
            Log.d(TAG, "get wifi config");
            String config = intent.getStringExtra(MyHostApduService.WIFI_CONFIG);
            mActivity.connectToWiFi(config);
        } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            Log.d(TAG, "connectivity action");
            ConnectivityManager conMan = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();
            if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.d(TAG, "Have Wifi Connection");
                isConnectToWifi = true;
            } else {
                Log.d(TAG, "Don't have Wifi Connection");
                isConnectToWifi = false;
            }
        }
    }

    public boolean isConnectToWifi() {
        return isConnectToWifi;
    }
}
