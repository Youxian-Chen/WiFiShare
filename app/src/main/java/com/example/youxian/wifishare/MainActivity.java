package com.example.youxian.wifishare;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();
    private WiFiApManager mWiFiApManager;
    private WifiConfiguration mWifiConfig;


    private ShareWifiFragment mShareWifiFragment;
    private AcceptWifiFragment mAcceptWifiFragment;
    private MainFragment mMainFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mWiFiApManager = new WiFiApManager(this);



        //setWifiConfig();
        //setWiFiHotSpot(mWifiConfig);
        connectToWiFiHotSpot();
    }

    private void initView() {
        replaceFragment(getMainFragment(), false);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.replace(R.id.container_main, fragment, "tag");
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    private Fragment getMainFragment() {
        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
            mMainFragment.setListener(new MainFragment.MainListener() {
                @Override
                public void onShareClick() {
                    replaceFragment(getShareWifiFragment(), true);
                }

                @Override
                public void onAcceptClick() {
                    replaceFragment(getAcceptWifiFragment(), true);
                }
            });
        }
        return mMainFragment;
    }

    private Fragment getShareWifiFragment() {
        if (mShareWifiFragment == null)
            mShareWifiFragment = new ShareWifiFragment();
        return mShareWifiFragment;
    }

    private Fragment getAcceptWifiFragment() {
        if (mAcceptWifiFragment == null)
            mAcceptWifiFragment = new AcceptWifiFragment();
        return mAcceptWifiFragment;
    }

    private void setWifiConfig() {
        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        mWifiConfig = new WifiConfiguration();
        mWifiConfig.SSID = "test5678";
        mWifiConfig.preSharedKey = "love0925";
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

    private void setWiFiHotSpot(WifiConfiguration conf) {
        boolean isEnabled = mWiFiApManager.setWifiApEnabled(conf, true);
        Log.d(TAG, "wifi ap enable: " + isEnabled);
        if (isEnabled) {

        }
    }

    private void connectToWiFiHotSpot() {
        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        mWifiConfig = new WifiConfiguration();
        mWifiConfig.SSID = "\"test5678\"";
        mWifiConfig.preSharedKey = "\"love0925\"";
        int res = wifiManager.addNetwork(mWifiConfig);
        Log.d("WifiPreference", "add Network returned " + res);
        wifiManager.disconnect();
        boolean isEnable = wifiManager.enableNetwork(res, true);
        Log.d("WifiPreference", "enable Network returned " + isEnable);
        wifiManager.reconnect();
    }
}
