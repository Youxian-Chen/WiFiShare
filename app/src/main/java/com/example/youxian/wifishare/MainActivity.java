package com.example.youxian.wifishare;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.youxian.wifishare.Fragment.AcceptFragment;
import com.example.youxian.wifishare.Fragment.ShareFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback{
    private static final String TAG = MainActivity.class.getName();
    private static final String Fragment_TAG = "fragment_tag";
    private static final byte[] CLA_INS_P1_P2 = { 0x00, (byte)0xA4, 0x04, 0x00 };
    private static final byte[] AID_ANDROID = { (byte)0xF0, 0x2, 0x03, 0x04, 0x05, 0x06, 0x07 };

    private WiFiApManager mWiFiApManager;
    private WifiConfiguration mWifiConfig;

    private ShareWifiFragment mShareWifiFragment;
    private AcceptWifiFragment mAcceptWifiFragment;

    private NfcAdapter mNfcAdapter;
    private IntentFilter mIntentFilter;
    private MainReceiver mReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mWiFiApManager = new WiFiApManager(this);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(MyHostApduService.WIFI_CONFIG);
        mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new MainReceiver(this);


        //setWifiConfig();
        //setWiFiHotSpot(mWifiConfig);
        //connectToWiFiHotSpot();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private void initView() {
        Button shareButton = (Button) findViewById(R.id.share_button_main);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ShareFragment(), true);
            }
        });

        Button acceptButton = (Button) findViewById(R.id.accept_button_main);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new AcceptFragment(), true);
            }
        });
    }

    public void openNfcReader() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter != null) {
            if (mNfcAdapter.isEnabled()) {
                mNfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A |
                        NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
            }
        }
    }

    public void closeNfcReader() {
        if (mNfcAdapter != null) {
            if (mNfcAdapter.isEnabled()) {
                mNfcAdapter.disableReaderMode(this);
            }
        }
    }

    private byte[] createSelectAidApdu(byte[] aid) {
        byte[] result = new byte[6 + aid.length];
        System.arraycopy(CLA_INS_P1_P2, 0, result, 0, CLA_INS_P1_P2.length);
        result[4] = (byte)aid.length;
        System.arraycopy(aid, 0, result, 5, aid.length);
        result[result.length - 1] = 0;
        return result;
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        IsoDep isoDep = IsoDep.get(tag);
        try {
            isoDep.connect();
            byte[] response = isoDep.transceive(createSelectAidApdu(AID_ANDROID));
            String resString = new String(response);
            Log.d(TAG, "select application response: " + resString);
            if (resString.equals("WiFiShare")) {
                isoDep.transceive(getShareWifiFragment().getWifiApConfig().getBytes());
                Log.d(TAG, "pass wifi hotspot config");
                mNfcAdapter.disableReaderMode(this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void connectToWiFi(String wifiConfig) {
        if (mReceiver.isConnectToWifi()) {
            Log.d(TAG, "already connect to wifi");
        } else {
            Log.d(TAG, "try to connect to wifi");
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            String[] separatedString = wifiConfig.split("-");
            WifiConfiguration mWifiConfig = new WifiConfiguration();
            mWifiConfig.SSID = "\"" + separatedString[0] + "\"";
            mWifiConfig.preSharedKey = "\"" + separatedString[1] + "\"";
            int res = wifiManager.addNetwork(mWifiConfig);
            Log.d(TAG, "add Network returned " + res);
            wifiManager.disconnect();
            boolean isEnable = wifiManager.enableNetwork(res, true);
            Log.d(TAG, "enable Network returned " + isEnable);
            wifiManager.reconnect();
            getAcceptWifiFragment().setStatusConnected();
        }
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_main, fragment, Fragment_TAG);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    private ShareWifiFragment getShareWifiFragment() {
        if (mShareWifiFragment == null)
            mShareWifiFragment = new ShareWifiFragment();
        return mShareWifiFragment;
    }

    private AcceptWifiFragment getAcceptWifiFragment() {
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
