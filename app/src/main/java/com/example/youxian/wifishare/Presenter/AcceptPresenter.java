package com.example.youxian.wifishare.Presenter;


/**
 * Created by Youxian on 1/13/16.
 */
public class AcceptPresenter {
    private static final String TAG = AcceptPresenter.class.getName();
    private static AcceptPresenter mInstance;

    private View mView;

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

    public interface View {
        void showConnectedStatus();
    }
}
