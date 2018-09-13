package com.example.kirill.shapp;

import android.support.v4.app.Fragment;

public class DashScreenActivity extends SingleFragmentActivity  {

    @Override
    protected Fragment createFragment() {
        return new DashScreenFragment();
    }
}
