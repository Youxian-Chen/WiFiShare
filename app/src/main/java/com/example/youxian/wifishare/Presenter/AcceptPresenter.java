package com.example.youxian.wifishare.Presenter;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
        if (checkWifiState(config)) {
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
    }

    private boolean checkWifiState(String config) {
        ConnectivityManager mConnectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            String[] separatedString = config.split("-");
            WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            String ssid = wifiManager.getConnectionInfo().getSSID().replaceAll("\"", "");
            if (separatedString[0].equals(ssid)) {
                return false;
            }
        }
        return true;
    }

    public interface View {
        void showConnectedStatus();
    }
}
