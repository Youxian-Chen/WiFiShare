package com.example.youxian.wifishare.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import com.example.youxian.wifishare.MainActivity;
import com.example.youxian.wifishare.WiFiApManager;

import java.io.IOException;

/**
 * Created by Youxian on 1/13/16.
 */
public class SharePresenter implements NfcAdapter.ReaderCallback {
    private static final String TAG = SharePresenter.class.getName();
    private static final byte[] CLA_INS_P1_P2 = { 0x00, (byte)0xA4, 0x04, 0x00 };
    private static final byte[] AID_ANDROID = { (byte)0xF0, 0x2, 0x03, 0x04, 0x05, 0x06, 0x07 };
    private static final String WIFI_SHARE = "WiFiShare";
    private static final String WIFI_SHARE_SSID = "WiFiShare_ssid";
    private static final String WIFI_SHARE_PASSWORD = "WiFiShare_password";

    private static SharePresenter mInstance;
    private Context mContext;
    private NfcAdapter mNfcAdapter;
    private View mView;
    private WiFiApManager mWiFiApManager;
    private WifiManager mWifiManager;
    private WifiConfiguration mWifiConfig;

    private String mSSID;
    private String mPassword;

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
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        mView.disableStopButton();
        getWifiConfig(context);
    }

    public void onResume(Context context) {
        mContext = context;
        if (mNfcAdapter != null) {
            if (mNfcAdapter.isEnabled()) {
                mNfcAdapter.enableReaderMode((MainActivity) context, this, NfcAdapter.FLAG_READER_NFC_A |
                        NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
            }
        }
    }

    public void onPause(Context context) {
        if (mNfcAdapter != null) {
            if (mNfcAdapter.isEnabled()) {
               mNfcAdapter.disableReaderMode((MainActivity) context);
            }
        }
    }

    public void validateConfig(String ssid, String password) {
        Log.d(TAG, ssid + ":" + password);
        if (!ssid.isEmpty() && !password.isEmpty() && password.length() >= 8) {
            mView.disableEditText();
            mView.disableShareButton();
            mView.enableStopButton();
            setWifiConfig(ssid, password);
            startWifiAp();
            storeWifiConfig(ssid, password);
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
        mSSID = ssid;
        mPassword = password;
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

    private void storeWifiConfig(String ssid, String password) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(WIFI_SHARE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(WIFI_SHARE_SSID, ssid).apply();
        sharedPreferences.edit().putString(WIFI_SHARE_PASSWORD, password).apply();
    }

    private void getWifiConfig(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(WIFI_SHARE, Context.MODE_PRIVATE);
        String ssid = sharedPreferences.getString(WIFI_SHARE_SSID, null);
        String password = sharedPreferences.getString(WIFI_SHARE_PASSWORD,  null);
        if (ssid != null && password != null) {
            mView.showWifiConfig(ssid, password);
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        IsoDep isoDep = IsoDep.get(tag);
        try {
            isoDep.connect();
            byte[] response = isoDep.transceive(createSelectAidApdu(AID_ANDROID));
            String resString = new String(response);
            Log.d(TAG, "select application response: " + resString);
            if (resString.equals(WIFI_SHARE)) {
                isoDep.transceive(getWifiApConfig().getBytes());
                Log.d(TAG, "pass wifi hotspot config");
            }

        } catch (IOException e) {
            e.printStackTrace();
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

    private String getWifiApConfig() {
        return mSSID + "-" + mPassword;
    }

    public interface View {
        void showAlertDialog();
        void disableEditText();
        void enableEditText();
        void disableShareButton();
        void enableShareButton();
        void disableStopButton();
        void enableStopButton();
        void showWifiConfig(String ssid, String password);
    }
}
