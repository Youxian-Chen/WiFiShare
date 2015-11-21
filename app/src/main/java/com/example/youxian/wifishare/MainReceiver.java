package com.example.youxian.wifishare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by Youxian on 11/21/15.
 */
public class MainReceiver extends BroadcastReceiver {
    private static final String TAG = MainReceiver.class.getName();
    private MainActivity mActivity;
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
        }
    }
}
