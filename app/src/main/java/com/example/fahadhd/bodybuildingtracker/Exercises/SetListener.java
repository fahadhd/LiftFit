
package com.example.fahadhd.bodybuildingtracker.exercises;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.sessions.Session;
import com.example.fahadhd.bodybuildingtracker.utilities.Utility;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.util.ArrayList;

//Button listener for sets. Each workout can have up to 8 set buttons.
public class SetListener implements View.OnClickListener {
    ExerciseActivity activity;
    ArrayList<Session> sessions;
    TextView workoutButton;
    long setWorkoutKey;
    int setNum, storedRep,shownRep, maxReps, maxSets;
    TrackerDAO dao;
    Workout curr_workout;
    Set currSet;
    WorkoutViewHolder viewHolder;
    LayoutInflater mInflater;

    public SetListener(TextView setButton, Workout curr_workout, Set currSet,
                       WorkoutViewHolder viewHolder, ExerciseActivity activity) {
        TrackerApplication application  = (TrackerApplication)activity.getApplication();
        this.activity = activity;
        this.dao = application.getDatabase();
        this.sessions = application.getSessions();
        this.workoutButton = setButton;
        this.setWorkoutKey = currSet.getWorkoutID();
        this.setNum = currSet.getOrderNum();
        this.shownRep = this.storedRep = currSet.getCurrRep();
        this.maxReps = curr_workout.getMaxReps();
        this.maxSets = curr_workout.getMaxSets();
        this.curr_workout = curr_workout;
        this.currSet = currSet;
        this.viewHolder = viewHolder;
        this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        //Displays the correct rep number of a set.
        if (storedRep > 0) {
            workoutButton.setText(Integer.toString(storedRep));
        }
    }

    @Override
    //Displays the correct text and timers when a set is pressed.
    public void onClick(View v) {
        //Updating the current rep and workout object.
        new UpdateSet().execute(shownRep);
        shownRep = (shownRep <= 0) ?  maxReps: shownRep-1;
        updateSession();

        //Display the default button when a set's rep number reaches 0.
        if (shownRep == 0) {
            workoutButton.setText(null);
            activity.stopTimerService();
            //viewHolder.completed_dialog.setText(null);
        } else {
            workoutButton.setText(Integer.toString(shownRep));
            boolean sets_started = Utility.allSetsStarted(curr_workout);
            boolean allFinished = (sets_started && Utility.allSetsFinished(curr_workout));
            this.initializeSnackbar(sets_started, allFinished);
        }
    }


    //Starts timer when set button is pressed.
    //TODO: Change "+5lb next time" to  "+{user preference lb/ki} next time!.
    public void initializeSnackbar(boolean sets_started, boolean allFinished) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        if(!sharedPref.getBoolean(activity.getString(R.string.timer_switch_key),true))
            return;
        if(!sets_started){
            //Since all sets aren't done, hide the congrats/failure message.
            //viewHolder.completed_dialog.setText(null);
            if(shownRep == maxReps) {
                activity.startTimerService("Nice job! Rest up for the next one.");
            }
            if(shownRep == maxReps-1) {
                activity.startTimerService("Rest a bit longer for the next one!");
            }
        }

        if(sets_started && !allFinished){
            // viewHolder.completed_dialog.setText(R.string.failed);
            activity.stopTimerService();
        }
        else if(allFinished){
            // viewHolder.completed_dialog.setText(R.string.congrats);
            activity.stopTimerService();
        }
    }

    public class UpdateSet extends AsyncTask<Integer,Void,Void>{

        @Override
        protected Void doInBackground(Integer... params) {
            dao.updateRep(setWorkoutKey, setNum, params[0], maxReps);
            // curr_workout = dao.getWorkout(curr_workout.getWorkoutID());
            return null;
        }
    }

    public void updateSession(){
        int position = sessions.size() - (int)curr_workout.getSessionID();
        ArrayList<Workout> workouts = sessions.get(position).getWorkouts();
        for(int i = 0; i < workouts.size(); i++){
            if(workouts.get(i).getWorkoutID() == curr_workout.getWorkoutID()){
                workouts.get(i).getSets().get(currSet.getOrderNum()-1).updateRep(shownRep);
            }
        }
    }

}

