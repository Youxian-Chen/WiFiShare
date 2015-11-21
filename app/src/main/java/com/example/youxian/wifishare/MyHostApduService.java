package com.example.youxian.wifishare;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Youxian on 11/21/15.
 */
public class MyHostApduService extends HostApduService {
    public static final String WIFI_CONFIG = "wifi_config";
    private static final String TAG = HostApduService.class.getName();
    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        if (selectAidApdu(commandApdu)) {
            Log.d(TAG, "application selected");
            return "WiFiShare".getBytes();
        } else {
            String mWifiCofig = new String(commandApdu);
            if (mWifiCofig.contains("-")) {
                Intent wifiConfigIntent = new Intent();
                wifiConfigIntent.setAction(WIFI_CONFIG);
                wifiConfigIntent.putExtra(WIFI_CONFIG, mWifiCofig);
                sendBroadcast(wifiConfigIntent);
            }
        }
        return new byte[0];
    }


    private boolean selectAidApdu(byte[] apdu) {
        return apdu.length >= 2 && apdu[0] == (byte)0 && apdu[1] == (byte)0xa4;
    }

    @Override
    public void onDeactivated(int reason) {

    }
}
