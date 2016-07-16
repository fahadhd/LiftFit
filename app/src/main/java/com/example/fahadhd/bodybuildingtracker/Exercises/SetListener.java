package com.example.fahadhd.bodybuildingtracker.Exercises;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.Utility;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;


public class SetListener implements View.OnClickListener {
    Button setButton;
    long setWorkoutKey;
    int setNum,currRep,maxReps,maxSets;
    TrackerDAO dao;
    Workout curr_workout;
    Set currSet;
    ViewHolder viewHolder;
    public SetListener(Button setButton, TrackerDAO dao, Workout curr_workout, Set currSet, ViewHolder viewHolder){
        this.setButton = setButton;
        this.setWorkoutKey = currSet.getWorkoutID();
        this.setNum = currSet.getOrderNum();
        this.currRep =  currSet.getCurrRep();
        this.maxReps = curr_workout.getMaxReps();
        this.maxSets = curr_workout.getMaxSets();
        this.dao = dao;
        this.curr_workout = curr_workout;
        this.currSet = currSet;
        this.viewHolder = viewHolder;

        if(currRep > 0) {
            setButton.setText(Integer.toString(currRep));
        }
    }
    @Override
    public void onClick(View v) {
        currRep = dao.updateRep(setWorkoutKey,setNum,currRep,maxReps);
        curr_workout = dao.updateWorkout(curr_workout);
        if(!Utility.allSetsStarted(curr_workout)) {
            viewHolder.completed_dialog.setText(null);
        }else {
            viewHolder.completed_dialog.setText("All sets done");
        }
        if(currRep == 0){
            setButton.setText(null);
        }
        else {
            setButton.setText(Integer.toString(currRep));
            boolean sets_started = false;
            boolean allFinished = false;
            Utility.startTimer(v, sets_started, allFinished, currRep, maxReps, maxSets);
        }
    }



}
