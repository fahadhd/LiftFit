package com.example.fahadhd.bodybuildingtracker.Exercises;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.Sessions.ViewSessionsFragment;
import com.example.fahadhd.bodybuildingtracker.Sessions.Session;
import com.example.fahadhd.bodybuildingtracker.Utility;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.util.ArrayList;
import java.util.List;


public class ExercisesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Workout>>{
    Session currentSession;
    TrackerDAO dao;
    ArrayList<Workout> workouts = new ArrayList<>();


    public ExercisesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new TrackerDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.exercises_list_fragment, container, false);
        Intent sessionIntent = getActivity().getIntent();

        //Get session information that was clicked on
        if(sessionIntent != null && sessionIntent.hasExtra(ViewSessionsFragment.INTENT_KEY)){

            currentSession= (Session)sessionIntent.getSerializableExtra
                    (ViewSessionsFragment.INTENT_KEY);
            setExistingWorkout(currentSession);
        }
        //New session was added
        else if (sessionIntent.hasExtra(MainActivity.ADD_TASK)){
            currentSession = Utility.addSession(dao);
            getActivity().setTitle("Today's Workout");
        }
        return rootView;
    }

   public void setExistingWorkout(Session session){
       String title = session.getDate() + "   Session  #"+session.getSessionId();
       getActivity().setTitle(title);

    }


    public void addWorkoutTask(String name, int weight, int max_sets, int max_reps){
        long id = currentSession.getSessionId();
        long workoutID = dao.addWorkout(id,workouts.size()+1,name,weight,max_sets);
        for(int i = 1; i <= max_sets; i++){
            dao.addSet(workoutID,i,max_reps,0);
        }
    }

    /////////////////// ASYNC LOADER FOR ADAPTER////////////////////
    //Loads all workout for current session in workouts list.
    @Override
    public Loader<List<Workout>> onCreateLoader(int id, Bundle args) {
        return new ExerciseLoader(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Workout>> loader, List<Workout> data) {
        workouts.addAll(data);

    }

    @Override
    public void onLoaderReset(Loader<List<Workout>> loader) {
        workouts.clear();

    }
}
