package com.example.youxian.wifishare.Presenter;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.youxian.wifishare.WiFiApManager;

/**
 * Created by Youxian on 1/13/16.
 */
public class SharePresenter {
    private static final String TAG = SharePresenter.class.getName();

    private static SharePresenter mInstance;

    private View mView;
    private WiFiApManager mWiFiApManager;
    private WifiManager mWifiManager;
    private WifiConfiguration mWifiConfig;

    public static SharePresenter getInstance() {
        if (mInstance == null) {
            mInstance = new SharePresenter();
        }
        return mInstance;
    }

    private SharePresenter() {

    }

    public void setView(View view) {
        mView = view;
    }

    public void initialize(Context context) {
        mWiFiApManager = new WiFiApManager(context);
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mView.disableStopButton();
    }

    public void validateConfig(String ssid, String password) {
        Log.d(TAG, ssid + ":" + password);
        if (!ssid.isEmpty() && !password.isEmpty() && password.length() >= 8) {
            mView.disableEditText();
            mView.disableShareButton();
            mView.enableStopButton();
            setWifiConfig(ssid, password);
            startWifiAp();
        } else {
            mView.showAlertDialog();
        }
    }

    public void cancelTethering() {
        mView.enableEditText();
        mView.enableShareButton();
        mView.disableStopButton();
        mWiFiApManager.setWifiApEnabled(mWifiConfig, false);
        mWifiManager.setWifiEnabled(true);
    }

    private void startWifiAp() {
        boolean isEnabled = mWiFiApManager.setWifiApEnabled(mWifiConfig, true);
        Log.d(TAG, "wifi ap enable: " + isEnabled);
    }

    private void setWifiConfig(String ssid, String password) {
        mWifiConfig = new WifiConfiguration();
        mWifiConfig.SSID = ssid;
        mWifiConfig.preSharedKey = password;
        mWifiConfig.hiddenSSID = true;
        mWifiConfig.status = WifiConfiguration.Status.ENABLED;
        mWifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        mWifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
    }

    public interface View {
        void showAlertDialog();
        void disableEditText();
        void enableEditText();
        void disableShareButton();
        void enableShareButton();
        void disableStopButton();
        void enableStopButton();
    }
}
