package com.example.youxian.wifishare;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Youxian on 11/17/15.
 */
public class AcceptWifiFragment extends Fragment {
    private TextView mStatus;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accept, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStatus = (TextView) view.findViewById(R.id.status_text_accept);
    }

    public void setStatusConnected() {
        mStatus.setText("Connect to wifi successfully");
    }
}
