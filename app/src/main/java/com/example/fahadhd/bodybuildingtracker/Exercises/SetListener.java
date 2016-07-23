package com.example.fahadhd.bodybuildingtracker.Exercises;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.Utility;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

//Button listener for sets. Each workout can have up to 8 set buttons.
public class SetListener implements View.OnClickListener {
    Button setButton;
    long setWorkoutKey;
    int setNum,currRep,maxReps,maxSets;
    TrackerDAO dao;
    Workout curr_workout;
    Set currSet;
    WorkoutViewHolder viewHolder;
    Context mContext;
    LayoutInflater mInflater;
    ExerciseActivity exerciseActivity;
    SetTimer setTimer;

    public SetListener(Button setButton, TrackerDAO dao, Workout curr_workout, Set currSet,
                       WorkoutViewHolder viewHolder, Context context){
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
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        this.exerciseActivity = (ExerciseActivity) mContext;
        this.setTimer = exerciseActivity.getSetTimer();



        //Displays the correct rep number of a set.
        if(currRep > 0) {
            setButton.setText(Integer.toString(currRep));
        }
    }
    @Override
    //Displays the correct text and timers when a set is pressed.
    public void onClick(View v) {
        //Updating the current rep and workout object.
        currRep = dao.updateRep(setWorkoutKey,setNum,currRep,maxReps);
        curr_workout = dao.updateWorkout(curr_workout);

        //Display the default button when a set's rep number reaches 0.
        if(currRep == 0){
            setButton.setText(null);
            setTimer.dismissSnackBar();
            viewHolder.completed_dialog.setText(null);

        }
        else {
            setButton.setText(Integer.toString(currRep));
            boolean sets_started = Utility.allSetsStarted(curr_workout);
            boolean allFinished = (sets_started && Utility.allSetsFinished(curr_workout));
            this.initializeSnackbar(v,sets_started, allFinished);
        }
    }


    //Starts timer when set button is pressed.
    //TODO: Change "+5lb next time" to  "+{user preference lb/ki} next time!.
    public void initializeSnackbar( View view, boolean sets_started, boolean allFinished){
        if(!sets_started){
            //Since all sets aren't done, hide the congrats/failure message.
            viewHolder.completed_dialog.setText(null);;
            if(currRep == maxReps) {
                setTimer.startTimer("Nice job! Rest up for the next one.",18000);
            }
            if(currRep == maxReps-1) {
                setTimer.startTimer("Rest a bit longer for the next one!",25000);
            }
        }

        if(sets_started && !allFinished){
            viewHolder.completed_dialog.setText(R.string.failed);
            setTimer.dismissSnackBar();
        }
        else if(allFinished){
            viewHolder.completed_dialog.setText(R.string.congrats);
            setTimer.dismissSnackBar();
        }
    }

}
