package com.example.kirill.shapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusFragment extends Fragment {
    private TextView issueFirst;
    private TextView issueSecond;
    private TextView issueThird;
    private TextView issueFourth;
    private ImageView resolvingFirst;
    private ImageView resolvingSecond;
    private ImageView resolvingThird;
    private ImageView resolvingFourth;

    private static int status;

    public static StatusFragment newInstance(int issueStatus) {
        status = issueStatus;
        return new StatusFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statusscreen_layout, container, false);
        issueFirst = view.findViewById(R.id.issueOne);
        issueSecond = view.findViewById(R.id.issueTwo);
        issueThird = view.findViewById(R.id.issueThree);
        issueFourth = view.findViewById(R.id.issueFour);
        resolvingFirst = view.findViewById(R.id.resolvedFirst);
        resolvingSecond = view.findViewById(R.id.resolvedSecond);
        resolvingThird = view.findViewById(R.id.resolvedThird);
        resolvingFourth = view.findViewById(R.id.resolvedFourth);
        setIssues();
        return view;
    }

    private void setIssues() {
        String issue;
        int idResolved;
        if (status == 1) {
            issue = "ISSUE!";
            idResolved = R.drawable.x;
        } else {
            issue = "NO ISSUES";
            idResolved = R.drawable.check;
        }
        issueFirst.setText(issue);
        issueSecond.setText(issue);
        issueThird.setText(issue);
        issueFourth.setText(issue);
        resolvingFirst.setImageResource(idResolved);
        resolvingSecond.setImageResource(idResolved);
        resolvingThird.setImageResource(idResolved);
        resolvingFourth.setImageResource(idResolved);
    }
}
