package com.example.fahadhd.bodybuildingtracker.Exercises;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        this.mInflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );


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
            viewHolder.completed_dialog.setText(null);

        }
        else {
            setButton.setText(Integer.toString(currRep));
            boolean sets_started = Utility.allSetsStarted(curr_workout);
            boolean allFinished = (sets_started && Utility.allSetsFinished(curr_workout));
            this.startTimer(v, sets_started, allFinished);
        }
    }
    //Starts timer when set button is pressed.
    //TODO: Change "+5lb next time" to  "+{user preference lb/ki} next time!.
    public void startTimer(View v, boolean sets_started, boolean allFinished){

        // Create the Snackbar
        final Snackbar snackbar = Snackbar.make(v, "Hello", Snackbar.LENGTH_LONG);
        // Customize snackbar view with my own.
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(Color.RED);
        View snackView = mInflater.inflate(R.layout.my_snackbar, null);
        TextView textViewTop = (TextView) snackView.findViewById(R.id.timer);
        textViewTop.setText("0:00");

        layout.addView(snackView, 0);

        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(Color.WHITE);


        if(!sets_started){
            viewHolder.completed_dialog.setText(null);
            if(currRep == maxReps) {
                snackbar.setText("Nice job! Rest up for the next one.").setDuration(18000);
                snackbar.show();
            }
            if(currRep == maxReps-1) {
                snackbar.setText("Rest a bit longer for the next one!").setDuration(25000);
                snackbar.show();
            }
        }
        if(sets_started && !allFinished){
            viewHolder.completed_dialog.setText(R.string.failed);
        }
        else if(allFinished){
            viewHolder.completed_dialog.setText(R.string.congrats);
        }
    }



}
