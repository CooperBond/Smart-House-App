package com.example.kirill.shapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FloorLab {
    private static FloorLab s_floor_lab;
    private List<FloorElement> m_floors;
    private DatabaseReference my_ref;
    private ArrayList current_user_data;

    public static FloorLab get(Context context) {
        if (s_floor_lab == null) {
            s_floor_lab = new FloorLab(context);
        }
        return s_floor_lab;
    }

    private FloorLab(Context context) {
        m_floors = new ArrayList<>();
        downloadDataFromFireBase();
        final FloorElement f1 = new FloorElement();
        f1.setFloor_name("Floor1");
        f1.setFire_mode(Parameters.status_fire_top);
        f1.setTemperature(Parameters.temperature_top);
        f1.setBackground_id(R.drawable.block);
        f1.setFloor_plan_resource(R.drawable.floorplan);
        f1.setSnow_mode(Parameters.status_snow_top);


        final FloorElement f2 = new FloorElement();
        f2.setFloor_name("Floor2");
        f2.setTemperature(Parameters.temperature_mid);
        f2.setBackground_id(R.drawable.block2);
        f2.setFire_mode(Parameters.status_fire_mid);
        f2.setFloor_plan_resource(R.drawable.floorplan2);
        f2.setSnow_mode(Parameters.status_snow_mid);


        final FloorElement f3 = new FloorElement();
        f3.setFloor_name("Floor3");
        f3.setTemperature(Parameters.temperature_bot);
        f3.setBackground_id(R.drawable.block3);
        f3.setFire_mode(Parameters.status_fire_bot);
        f3.setFloor_plan_resource(R.drawable.floorplan3);
        f3.setSnow_mode(Parameters.status_snow_bot);


        final FloorElement f4 = new FloorElement();
        f4.setFloor_name("Floor4");
        f4.setTemperature(Parameters.temperature_4th);
        f4.setBackground_id(R.drawable.block4);
        f4.setFire_mode(Parameters.status_fire_4th);
        f4.setFloor_plan_resource(R.drawable.floorplan4);
        f4.setSnow_mode(Parameters.status_snow_4th);


        final FloorElement f5 = new FloorElement();
        f5.setFloor_name("Floor5");
        f5.setBackground_id(R.drawable.block5);
        f5.setFloor_plan_resource(R.drawable.floorplan5);
        f5.setTemperature(Parameters.temperature_5th);
        f5.setFire_mode(Parameters.status_fire_5th);
        f5.setSnow_mode(Parameters.status_snow_5th);


        m_floors.add(f1);
        m_floors.add(f2);
        m_floors.add(f3);
        m_floors.add(f4);
        m_floors.add(f5);

    }
    public void downloadDataFromFireBase(){
        try {
            my_ref = FirebaseDatabase.getInstance().getReference();
        } catch (Exception e) {
            e.printStackTrace();
        }
        my_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList userData = (ArrayList) dataSnapshot.child(SettingsAuth.user_id).getValue();
                current_user_data = userData;
                    Parameters.temperature_top = Integer.parseInt(String.valueOf(userData.get(0)));
                    Parameters.temperature_mid = Integer.parseInt(String.valueOf(userData.get(1)));
                    Parameters.temperature_bot = Integer.parseInt(String.valueOf(userData.get(2)));
                    Parameters.temperature_4th = Integer.parseInt(String.valueOf(userData.get(3)));
                    Parameters.temperature_5th = Integer.parseInt(String.valueOf(userData.get(4)));

                    Parameters.status_fire_top = Integer.parseInt(String.valueOf(userData.get(5)));
                    Parameters.status_fire_mid = Integer.parseInt(String.valueOf(userData.get(6)));
                    Parameters.status_fire_bot = Integer.parseInt(String.valueOf(userData.get(7)));
                    Parameters.status_fire_4th = Integer.parseInt(String.valueOf(userData.get(8)));
                    Parameters.status_fire_5th = Integer.parseInt(String.valueOf(userData.get(9)));
                    Parameters.status_snow_top = Integer.parseInt(String.valueOf(userData.get(10)));
                    Parameters.status_snow_mid = Integer.parseInt(String.valueOf(userData.get(11)));
                    Parameters.status_snow_bot = Integer.parseInt(String.valueOf(userData.get(12)));
                    Parameters.status_snow_4th = Integer.parseInt(String.valueOf(userData.get(13)));
                    Parameters.status_snow_5th = Integer.parseInt(String.valueOf(userData.get(14)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    public Map<String, Object> toMap(int position, Object value) {
        HashMap<String, Object> result = new HashMap<>();
        ArrayList data = current_user_data;
        data.set(position, value);
        result.put(SettingsAuth.user_id, data);
        return result;
    }

    public void updateData(String floorName, int temp, int fireStatus, int snowStatus) {
        if (floorName.equals("Floor1")) {
            my_ref.updateChildren(toMap(0, temp));
            my_ref.updateChildren(toMap(5, fireStatus));
            my_ref.updateChildren(toMap(10, snowStatus));
        } else if (floorName.equals("Floor2")) {
            my_ref.updateChildren(toMap(1, temp));
            my_ref.updateChildren(toMap(6, fireStatus));
            my_ref.updateChildren(toMap(11, snowStatus));
        } else if (floorName.equals("Floor3")) {
            my_ref.updateChildren(toMap(2, temp));
            my_ref.updateChildren(toMap(7, fireStatus));
            my_ref.updateChildren(toMap(12, snowStatus));
        } else if (floorName.equals("Floor4")) {
            my_ref.updateChildren(toMap(3, temp));
            my_ref.updateChildren(toMap(8, fireStatus));
            my_ref.updateChildren(toMap(13, snowStatus));
        } else if (floorName.equals("Floor5")) {
            my_ref.updateChildren(toMap(4, temp));
            my_ref.updateChildren(toMap(9, fireStatus));
            my_ref.updateChildren(toMap(14, snowStatus));
        }

    }

    public List<FloorElement> getFloors() {
        return m_floors;
    }

    public FloorElement getFloor(String name) {
        for (FloorElement floorElement : m_floors) {
            if (floorElement.getFloor_name().equals(name)) {
                return floorElement;
            }
        }
        return null;
    }
}
