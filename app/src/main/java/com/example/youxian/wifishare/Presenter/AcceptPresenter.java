package com.example.youxian.wifishare.Presenter;


import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by Youxian on 1/13/16.
 */
public class AcceptPresenter {
    private static final String TAG = AcceptPresenter.class.getName();
    private static AcceptPresenter mInstance;

    private View mView;
    private Context mContext;

    private AcceptPresenter() {

    }

    public static AcceptPresenter getInstance() {
        if (mInstance == null) {
            mInstance = new AcceptPresenter();
        }
        return mInstance;
    }

    public void setView(View view) {
        mView = view;
    }

    public void initialize(Context context) {
        mContext = context;
    }

    public void connectToWifi(String config) {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        String[] separatedString = config.split("-");
        WifiConfiguration mWifiConfig = new WifiConfiguration();
        mWifiConfig.SSID = "\"" + separatedString[0] + "\"";
        mWifiConfig.preSharedKey = "\"" + separatedString[1] + "\"";
        int res = wifiManager.addNetwork(mWifiConfig);
        Log.d(TAG, "add Network returned " + res);
        wifiManager.disconnect();
        boolean isEnable = wifiManager.enableNetwork(res, true);
        Log.d(TAG, "enable Network returned " + isEnable);
        wifiManager.reconnect();
        mView.showConnectedStatus();
    }

    public interface View {
        void showConnectedStatus();
    }
}
