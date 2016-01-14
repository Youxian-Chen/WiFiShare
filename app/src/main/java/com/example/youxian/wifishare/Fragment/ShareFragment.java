package com.example.youxian.wifishare.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.youxian.wifishare.Presenter.SharePresenter;
import com.example.youxian.wifishare.R;

/**
 * Created by Youxian on 1/13/16.
 */
public class ShareFragment extends Fragment implements SharePresenter.View {
    private static final String TAG = ShareFragment.class.getName();

    private EditText ssidEditText;
    private EditText passwordEditText;
    private Button enableButton;
    private Button disbleButton;

    private SharePresenter mSharePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharePresenter = SharePresenter.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSharePresenter.setView(this);
        ssidEditText = (EditText) view.findViewById(R.id.ssid_edit_share);
        passwordEditText = (EditText) view.findViewById(R.id.password_edit_share);
        enableButton = (Button) view.findViewById(R.id.enable_button_share);
        enableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharePresenter.validateConfig(ssidEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
        disbleButton = (Button) view.findViewById(R.id.disable_button_share);
        disbleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharePresenter.cancelTethering();
            }
        });
        View container = view.findViewById(R.id.share_container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });
        mSharePresenter.initialize(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSharePresenter.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharePresenter.onPause(getActivity());
    }

    @Override
    public void showAlertDialog() {
        Log.d(TAG, "showAlertDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alert_title);
        builder.setMessage(R.string.alert_text);
        builder.setPositiveButton(R.string.alert_ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    @Override
    public void disableEditText() {
        ssidEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
    }

    @Override
    public void enableEditText() {
        ssidEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
    }

    @Override
    public void disableShareButton() {
        enableButton.setEnabled(false);
    }

    @Override
    public void enableShareButton() {
        enableButton.setEnabled(true);
    }

    @Override
    public void disableStopButton() {
        disbleButton.setEnabled(false);
    }

    @Override
    public void enableStopButton() {
        disbleButton.setEnabled(true);
    }

    @Override
    public void showWifiConfig(String ssid, String password) {
        ssidEditText.setText(ssid);
        passwordEditText.setText(password);
    }
}
