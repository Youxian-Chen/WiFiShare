package com.example.youxian.wifishare;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Youxian on 11/17/15.
 */
public class MainFragment extends Fragment {
    private Button shareButton;
    private Button acceptButton;

    private MainListener mMainListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shareButton = (Button) view.findViewById(R.id.share_button_main);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainListener != null)
                    mMainListener.onShareClick();
            }
        });
        acceptButton = (Button) view.findViewById(R.id.accept_button_main);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainListener != null)
                    mMainListener.onAcceptClick();
            }
        });
    }

    public void setListener(MainListener listener) {
        mMainListener = listener;
    }

    public interface MainListener {
        void onShareClick();
        void onAcceptClick();
    }
}
