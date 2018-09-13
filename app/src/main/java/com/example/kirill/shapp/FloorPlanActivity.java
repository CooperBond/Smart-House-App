package com.example.kirill.shapp;

import android.support.v4.app.Fragment;

public class FloorPlanActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        int status = getIntent().getIntExtra("status",0);
        int image_resource= getIntent().getIntExtra("image",0);
        return FloorPlanFragment.newInstance(status, image_resource);
    }
}
