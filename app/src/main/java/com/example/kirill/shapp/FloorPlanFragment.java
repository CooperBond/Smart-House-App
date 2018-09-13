package com.example.kirill.shapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FloorPlanFragment extends Fragment {
    private static int status;
    private static int image_resource;
    private ImageView background_image_plan;
    private ImageView alert;

    public static FloorPlanFragment newInstance(int issueStatus, int floorPlanImageResource) {
        status = issueStatus;
        image_resource = floorPlanImageResource;
        return new FloorPlanFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.floorplan_layout, container, false);
        background_image_plan = view.findViewById(R.id.floorimage);
        background_image_plan.setImageResource(image_resource);
        alert = view.findViewById(R.id.alertImage);
        setData();
        return view;
    }

    private void setData() {
        if (status == 1){
            alert.setVisibility(View.VISIBLE);
        }else{
            alert.setVisibility(View.INVISIBLE);
        }
    }
}
