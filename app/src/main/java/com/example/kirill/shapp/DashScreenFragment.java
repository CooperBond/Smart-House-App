package com.example.kirill.shapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DashScreenFragment extends Fragment {
    private RecyclerView m_recycle_view;
    private FloorAdapter m_adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view, container, false);
        m_recycle_view = view.findViewById(R.id.recycler_view);
        m_recycle_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }



    private void updateUI() {
        FloorLab floorLab = FloorLab.get(getActivity());
        List<FloorElement> floors = floorLab.getFloors();
        floorLab.downloadDataFromFireBase();
        m_adapter = new FloorAdapter(floors);
        m_recycle_view.setAdapter(m_adapter);
    }

    private class FloorHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout;
        private FloorElement m_floor;
        private ImageView temperature_indicator;
        private ImageView snow_mode;
        private ImageView fire_mode;
        private ImageView issue_indicator;
        private TextView floor_plan_button;
        private ImageView increase_button;
        private ImageView decrease_button;
        private FloorLab floor_lab = FloorLab.get(getActivity());

        public void bind(FloorElement floor) {
            m_floor = floor;
            setData(m_floor.getTemperature(),
                    m_floor.getFire_mode(),
                    m_floor.getSnow_mode(),
                    m_floor.getIssue_status(),
                    m_floor.getBackground_id());
            createIssueForTesting(m_floor.getTemperature());
        }

        private void setData(int temperature,
                             int fireMode,
                             int snowMode,
                             int issueStatus,
                             int backgroundId) {
            setBackgroundTheme(backgroundId);
            setTemperature_indicator(temperature);
            setFireAndSnowModes(fireMode, snowMode);
            checkIssues(issueStatus);
        }

        private void setBackgroundTheme(int id) {
            layout.setBackground(getResources().getDrawable(id));
        }

        private void checkIssues(int issue_status) {
            if (issue_status == 1) {
                issue_indicator.setImageResource(R.drawable.issue_appear);
            } else {
                issue_indicator.setImageResource(R.drawable.issue_noiss);
            }
        }

        private void createIssueForTesting(int temp) {
            if (temp == 18 || temp == 30) {
                m_floor.setIssue_status(1);
                checkIssues(m_floor.getIssue_status());
            } else {
                m_floor.setIssue_status(0);
                checkIssues(m_floor.getIssue_status());
            }
        }

        private void setTemperature_indicator(int temp) {
            if (temp == 16 || temp == 0) {
                temperature_indicator.setImageResource(R.drawable.sixteen);
            } else if (temp == 17) {
                temperature_indicator.setImageResource(R.drawable.seventeen);
            } else if (temp == 18) {
                temperature_indicator.setImageResource(R.drawable.eigthteen);
            } else if (temp == 19) {
                temperature_indicator.setImageResource(R.drawable.nineteen);
            } else if (temp == 20) {
                temperature_indicator.setImageResource(R.drawable.twenty);
            } else if (temp == 21) {
                temperature_indicator.setImageResource(R.drawable.twentyone);
            } else if (temp == 22) {
                temperature_indicator.setImageResource(R.drawable.twentytwo);
            } else if (temp == 23) {
                temperature_indicator.setImageResource(R.drawable.twentythree);
            } else if (temp == 24) {
                temperature_indicator.setImageResource(R.drawable.twentyfour);
            } else if (temp == 25) {
                temperature_indicator.setImageResource(R.drawable.twentyfive);
            } else if (temp == 26) {
                temperature_indicator.setImageResource(R.drawable.twentysix);
            } else if (temp == 27) {
                temperature_indicator.setImageResource(R.drawable.twentyseven);
            } else if (temp == 28) {
                temperature_indicator.setImageResource(R.drawable.twentyeigth);
            } else if (temp == 29) {
                temperature_indicator.setImageResource(R.drawable.twentynine);
            } else if (temp == 30) {
                temperature_indicator.setImageResource(R.drawable.thirty);
            } else if (temp == 31) {
                temperature_indicator.setImageResource(R.drawable.thirtyone);
            }
        }

        private void setFireAndSnowModes(int fire_indicator, int snow_indicator) {
            if (fire_indicator == 0 && snow_indicator != 0) {
                snow_mode.setImageResource(R.drawable.snow_act);
                fire_mode.setImageResource(R.drawable.fire_noact);
            } else if (snow_indicator == 0 && fire_indicator != 0) {
                snow_mode.setImageResource(R.drawable.snow_noact);
                fire_mode.setImageResource(R.drawable.fire_active);
            } else {
                snow_mode.setImageResource(R.drawable.snow_noact);
                fire_mode.setImageResource(R.drawable.fire_noact);
            }
        }

        public FloorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.floor_list_fragment, parent, false));
            layout = itemView.findViewById(R.id.floorsLayout);
            temperature_indicator = itemView.findViewById(R.id.temperatureIndicator);
            floor_plan_button = itemView.findViewById(R.id.floorPlanButton);
            floor_plan_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),FloorPlanActivity.class);
                    intent.putExtra("status", m_floor.getIssue_status());
                    intent.putExtra("image", m_floor.getFloor_plan_resource());
                    startActivity(intent);
                }
            });
            snow_mode = itemView.findViewById(R.id.snowModeIndicator);
            snow_mode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_floor.getSnow_mode() == 1) {
                        m_floor.setSnow_mode(0);
                    } else {
                        m_floor.setSnow_mode(1);
                        m_floor.setFire_mode(0);
                    }
                    setFireAndSnowModes(m_floor.getFire_mode(), m_floor.getSnow_mode());
                    floor_lab.updateData(m_floor.getFloor_name(),
                            m_floor.getTemperature(),
                            m_floor.getFire_mode(),
                            m_floor.getSnow_mode());
                }
            });
            fire_mode = itemView.findViewById(R.id.fireModeIndicator);
            fire_mode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_floor.getFire_mode() == 1) {
                        m_floor.setFire_mode(0);
                    } else {
                        m_floor.setFire_mode(1);
                        m_floor.setSnow_mode(0);
                    }
                    setFireAndSnowModes(m_floor.getFire_mode(), m_floor.getSnow_mode());
                    floor_lab.updateData(m_floor.getFloor_name(),
                            m_floor.getTemperature(),
                            m_floor.getFire_mode(),
                            m_floor.getSnow_mode());
                }
            });
            issue_indicator = itemView.findViewById(R.id.issueIndicator);
            issue_indicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), StatusActivity.class);
                    intent.putExtra("issueStatus", m_floor.getIssue_status());
                    startActivity(intent);
                }
            });
            increase_button = itemView.findViewById(R.id.buttonIncrease);
            increase_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_floor.getTemperature() < 31) {
                        m_floor.setTemperature(m_floor.getTemperature() + 1);
                    } else {
                        m_floor.setTemperature(31);
                    }
                    setTemperature_indicator(m_floor.getTemperature());
                    createIssueForTesting(m_floor.getTemperature());
                    checkIssues(m_floor.getIssue_status());
                    floor_lab.updateData(m_floor.getFloor_name(),
                            m_floor.getTemperature(),
                            m_floor.getFire_mode(),
                            m_floor.getSnow_mode());
                }
            });
            decrease_button = itemView.findViewById(R.id.buttonDecrease);
            decrease_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_floor.getTemperature() > 16) {
                        m_floor.setTemperature(m_floor.getTemperature() - 1);
                    } else {
                        m_floor.setTemperature(16);
                    }
                    setTemperature_indicator(m_floor.getTemperature());
                    createIssueForTesting(m_floor.getTemperature());
                    checkIssues(m_floor.getIssue_status());
                    floor_lab.updateData(m_floor.getFloor_name(),
                            m_floor.getTemperature(),
                            m_floor.getFire_mode(),
                            m_floor.getSnow_mode());
                }
            });
        }
    }

    private class FloorAdapter extends RecyclerView.Adapter<FloorHolder> {
        private List<FloorElement> mFloors;

        public FloorAdapter(List<FloorElement> floors) {
            mFloors = floors;
        }

        @NonNull
        @Override
        public FloorHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FloorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FloorHolder floorHolder, int i) {
            FloorElement floorElement = mFloors.get(i);
            floorHolder.bind(floorElement);
        }

        @Override
        public int getItemCount() {
            return mFloors.size();
        }
    }
}
