package com.example.youxian.wifishare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import com.example.youxian.wifishare.Fragment.AcceptFragment;
import com.example.youxian.wifishare.Fragment.ShareFragment;


public class MainActivity extends AppCompatActivity {
    private static final String Fragment_TAG = "fragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_main, fragment, Fragment_TAG);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }
}
