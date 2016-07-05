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
    TrackerDAO dao;
    ArrayList<Workout> workouts = new ArrayList<>();
    ListView exerciseListView;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(R.id.exercise_loader_id,null,this);
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
        adapter = new ExerciseAdapter(getActivity(),workouts);

        Intent sessionIntent = getActivity().getIntent();

        //Get session information from main activity
        if(sessionIntent != null && sessionIntent.hasExtra(ViewSessionsFragment.INTENT_KEY)) {

            currentSession = (Session) sessionIntent.getSerializableExtra
                    (ViewSessionsFragment.INTENT_KEY);
            setExistingWorkout(currentSession);
        }
        else if (sessionIntent != null && sessionIntent.hasExtra(MainActivity.ADD_TASK)){
            currentSession = Utility.addSession(dao);
            getActivity().setTitle("Today's Workout");
        }

        exerciseListView = (ListView)rootView.findViewById(R.id.exercises_list_main);
        exerciseListView.setAdapter(adapter);

        return rootView;
    }

   public void setExistingWorkout(Session session){
       String title = session.getDate() + "   Session  #"+session.getSessionId();
       getActivity().setTitle(title);
    }
    public long getSessionID(){
        return currentSession.getSessionId();
    }



    /////////////////// ASYNC LOADER FOR ADAPTER////////////////////
    //Loads all workout for current session in workouts list.
    @Override
    public Loader<List<Workout>> onCreateLoader(int id, Bundle args) {
        long sessionID = currentSession.getSessionId();
        return new ExerciseLoader(getActivity().getApplicationContext(),dao,sessionID);
    }

    @Override
    public void onLoadFinished(Loader<List<Workout>> loader, List<Workout> data) {
        if(workouts.isEmpty()){ workouts.addAll(data);}
        else {
            workouts.add(data.get(data.size()-1));
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<List<Workout>> loader) {
        workouts.clear();

    }

}
