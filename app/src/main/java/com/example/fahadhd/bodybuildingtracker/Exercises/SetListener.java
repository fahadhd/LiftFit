package com.example.fahadhd.bodybuildingtracker.Exercises;

import android.view.View;
import android.widget.Button;

import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;


public class SetListener implements View.OnClickListener {
    Button setButton;
    long setWorkoutKey;
    int setNum,currRep,maxRep;
    TrackerDAO dao;
    public SetListener(Button setButton, TrackerDAO dao, Workout curr_workout, Set currSet){
        this.setButton = setButton;
        this.setWorkoutKey = currSet.getWorkoutID();
        this.setNum = currSet.getOrderNum();
        this.currRep =  currSet.getCurrRep();
        this.maxRep = curr_workout.getMaxReps();
        this.dao = dao;

        if(currRep != -1) {
            setButton.setText(Integer.toString(currRep));
        }
    }
    @Override
    public void onClick(View v) {
        currRep = dao.updateRep(setWorkoutKey,setNum,currRep,maxRep);
        setButton.setText(Integer.toString(currRep));
    }
    //Checks to see if a current workout has all sets finished(ie all sets curr rep is at max)
     public boolean allFinished(){
        return false;
     }
}
