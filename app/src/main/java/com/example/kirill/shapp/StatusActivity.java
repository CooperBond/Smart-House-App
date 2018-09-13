package com.example.kirill.shapp;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class StatusActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        int status = getIntent().getIntExtra("issueStatus",0);
        return StatusFragment.newInstance(status);
    }
}
