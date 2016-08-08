package com.example.fahadhd.bodybuildingtracker.Exercises;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.Sessions.SessionsFragment;
import com.example.fahadhd.bodybuildingtracker.Sessions.Session;
import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.util.ArrayList;
import java.util.List;


public class ExercisesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Workout>>{
    TrackerDAO dao;
    ExerciseAdapter adapter;
    Session currentSession;
    long sessionID;
    int positon;
    ArrayList<Session> sessions;
    ArrayList<Workout> workouts = new ArrayList<>();
    ListView exerciseListView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrackerApplication application  = (TrackerApplication)getActivity().getApplication();
        dao = application.getDatabase();
        sessions = application.getSessions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.exercises_list_fragment, container, false);
        adapter = new ExerciseAdapter(getActivity(),workouts,dao);


        Intent sessionIntent = getActivity().getIntent();

        //Get session information from main activity
        if(sessionIntent != null && sessionIntent.hasExtra(SessionsFragment.INTENT_KEY)) {

            currentSession = (Session) sessionIntent.getSerializableExtra
                    (SessionsFragment.INTENT_KEY);
            positon = sessionIntent.getIntExtra(SessionsFragment.POSITION_KEY,0);
            setExistingWorkout(currentSession);
        }
        else if (sessionIntent != null && sessionIntent.hasExtra(MainActivity.ADD_TASK)){
            currentSession = (Session) sessionIntent.getSerializableExtra
                    (MainActivity.ADD_TASK);
            //Add new session to cached data-+3*9
            sessions.add(0,currentSession);
            positon = 0;
            getActivity().setTitle("Current Workout");
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


    public void refreshSessionData(){
        Session updatedSession = dao.getSession(sessionID);
        sessions.set(positon,updatedSession);
    }


    /*************** ASYNC LOADER FOR ADAPTER********************/
    //Loads all workouts for current session in workouts list.
    @Override
    public Loader<List<Workout>> onCreateLoader(int id, Bundle args) {
        return new ExerciseLoader(getActivity().getApplicationContext(),dao,sessionID);
    }

    @Override
    public void onLoadFinished(Loader<List<Workout>> loader, List<Workout> data) {
        if(workouts.size() == 0) workouts.addAll(data);
        else{
            workouts.add(data.get(data.size()-1));
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<List<Workout>> loader) {
        workouts.clear();
    }
    /************************************************************/

}
