package com.example.fahadhd.bodybuildingtracker.Exercises;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.Sessions.ViewSessionsFragment;
import com.example.fahadhd.bodybuildingtracker.Sessions.Session;
import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.Utility;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.util.ArrayList;
import java.util.List;


public class ExercisesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Workout>>{
    ExerciseAdapter adapter;
    Session currentSession;
    long sessionID;
    TrackerDAO dao;
    ArrayList<Workout> workouts = new ArrayList<>();
    ListView exerciseListView;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrackerApplication application  = (TrackerApplication)getActivity().getApplication();
        dao = application.getDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.exercises_list_fragment, container, false);
        adapter = new ExerciseAdapter(getActivity(),workouts,dao);


        Intent sessionIntent = getActivity().getIntent();

        //Get session information from main activity
        if(sessionIntent != null && sessionIntent.hasExtra(ViewSessionsFragment.INTENT_KEY)) {

            currentSession = (Session) sessionIntent.getSerializableExtra
                    (ViewSessionsFragment.INTENT_KEY);
            setExistingWorkout(currentSession);
        }
        else if (sessionIntent != null && sessionIntent.hasExtra(MainActivity.ADD_TASK)){
            currentSession = (Session) sessionIntent.getSerializableExtra
                    (MainActivity.ADD_TASK);
            getActivity().setTitle("Today's Workout");
        }
        sessionID = currentSession.getSessionId();

        exerciseListView = (ListView)rootView.findViewById(R.id.exercises_list_main);
        exerciseListView.setAdapter(adapter);

        return rootView;
    }

   public void setExistingWorkout(Session session){
       String title = session.getDate() + "   Session  #"+session.getSessionId();
       getActivity().setTitle(title);
    }

    public void addWorkoutTask(String name, int weight, int max_sets, int max_reps){
        long workoutID = dao.addWorkout(sessionID,workouts.size()+1,name,weight,max_sets,max_reps);
        for(int i = 1; i <= max_sets; i++){
            //Initializes each set in a current workout
            dao.addSet(workoutID,i);
        }
    }



    /////////////////// ASYNC LOADER FOR ADAPTER////////////////////
    //Loads all workouts for current session in workouts list.
    @Override
    public Loader<List<Workout>> onCreateLoader(int id, Bundle args) {
        return new ExerciseLoader(getActivity().getApplicationContext(),dao,sessionID);
    }

    @Override
    public void onLoadFinished(Loader<List<Workout>> loader, List<Workout> data) {
        //TODO: Cache data when loader restarts
        workouts.clear();
        workouts.addAll(data);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<List<Workout>> loader) {
        workouts.clear();
    }

}
