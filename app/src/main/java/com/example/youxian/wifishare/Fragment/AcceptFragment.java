package com.example.youxian.wifishare.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.youxian.wifishare.Presenter.AcceptPresenter;
import com.example.youxian.wifishare.R;

/**
 * Created by Youxian on 1/13/16.
 */
public class AcceptFragment extends Fragment implements AcceptPresenter.View {

    private TextView statusText;

    private AcceptPresenter mAcceptPresenter;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAcceptPresenter = AcceptPresenter.getInstance();
        statusText = (TextView) view.findViewById(R.id.status_text_accept);
        View container = view.findViewById(R.id.accept_container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });
        mAcceptPresenter.setView(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void showConnectedStatus() {
        statusText.setText(R.string.status_text);
    }
}
