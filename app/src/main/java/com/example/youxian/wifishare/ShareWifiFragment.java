package com.example.youxian.wifishare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Youxian on 11/17/15.
 */
public class ShareWifiFragment extends Fragment {
    private static final String TAG = ShareWifiFragment.class.getName();

    private EditText ssidEditText;
    private EditText passwordEditText;
    private Button shareButton;
    private Button stopButton;
    private WiFiApManager mWiFiApManager;
    private WifiConfiguration mWifiConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ssidEditText = (EditText) view.findViewById(R.id.ssid_edit_share);
        passwordEditText = (EditText) view.findViewById(R.id.password_edit_share);
        shareButton = (Button) view.findViewById(R.id.enable_button_share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "share button clicked");
                if (checkWrongApData()) {
                    Log.d(TAG, "check failed");
                    getAlertDialog(getActivity());
                } else {
                    Log.d(TAG, "check ok");
                    ssidEditText.setEnabled(false);
                    passwordEditText.setEnabled(false);
                    startWifiAp();
                    stopButton.setEnabled(true);
                    shareButton.setEnabled(false);
                    ((MainActivity)getActivity()).openNfcReader();
                }
            }
        });
        stopButton = (Button) view.findViewById(R.id.disable_button_share);
        stopButton.setEnabled(false);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "stop sharing button clicked");
                if (mWifiConfig != null) {
                    mWiFiApManager.setWifiApEnabled(mWifiConfig, false);
                    mWifiConfig = null;
                    shareButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    ((MainActivity)getActivity()).closeNfcReader();
                }
            }
        });
    }

    private void getAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("invalid settings");
        builder.setMessage("id and password can't be empty and password at least has 8 chs");
        builder.setPositiveButton("okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();

    }

    private boolean checkWrongApData() {
        Log.d(TAG, passwordEditText.getText().length()+"");
        Log.d(TAG, passwordEditText.getText().toString().length()+"");
        Log.d(TAG, passwordEditText.getText().toString());
        if (ssidEditText.getText().toString().isEmpty()) {
            return true;
        } else if (passwordEditText.getText().toString().isEmpty()) {
            return true;
        } else if (passwordEditText.getText().toString().length() < 8) {
            return true;
        }
        return false;
    }

    private void startWifiAp() {
        setWifiConfig();
        mWiFiApManager = new WiFiApManager(getActivity());
        boolean isEnabled = mWiFiApManager.setWifiApEnabled(mWifiConfig, true);
        Log.d(TAG, "wifi ap enable: " + isEnabled);
    }

    private void setWifiConfig() {
        mWifiConfig = new WifiConfiguration();
        mWifiConfig.SSID = ssidEditText.getText().toString();
        mWifiConfig.preSharedKey = passwordEditText.getText().toString();
        mWifiConfig.hiddenSSID = true;
        mWifiConfig.status = WifiConfiguration.Status.ENABLED;
        mWifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        mWifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        mWifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        mWifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        mWifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        Log.d(TAG, "ID: " + mWifiConfig.SSID);
        Log.d(TAG, "password: " + mWifiConfig.preSharedKey);
    }

    public String getWifiApConfig() {
        return mWifiConfig.SSID + "-" + mWifiConfig.preSharedKey;
    }
}
