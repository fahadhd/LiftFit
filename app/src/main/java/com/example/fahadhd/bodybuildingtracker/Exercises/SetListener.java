package com.example.fahadhd.bodybuildingtracker.Exercises;

import android.view.View;
import android.widget.Button;

import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;


public class SetListener implements View.OnClickListener {
    Button setButton;
    long workoutKey;
    int orderNum,currRep,maxRep;
    TrackerDAO dao;
    public SetListener(Button setButton, long workoutKey, int orderNum, int currRep,
                       int maxRep, TrackerDAO dao){
        this.setButton = setButton;
        this.workoutKey = workoutKey;
        this.orderNum = orderNum;
        this.currRep = currRep;
        this.maxRep = maxRep;
        this.dao = dao;

        if(currRep != -1) {
            setButton.setText(Integer.toString(currRep));
        }
    }
    @Override
    public void onClick(View v) {
        currRep = dao.updateRep(workoutKey,orderNum,currRep,maxRep);
        setButton.setText(Integer.toString(currRep));
    }
}
