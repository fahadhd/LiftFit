package com.example.fahadhd.bodybuildingtracker.Exercises;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.Sessions.ViewSessionsFragment;
import com.example.fahadhd.bodybuildingtracker.Sessions.Session;
import com.example.fahadhd.bodybuildingtracker.Utility;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


public class ExercisesFragment extends Fragment {
    Session currentSession;
    TrackerDAO dao;


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


    public void addWorkoutTask(){

    }
    public class AddWorkoutTask extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... params) {

            return null;
        }

        public void addNewWorkout(String name, int weight, int max_sets, int max_reps){
            TrackerDAO db = new TrackerDAO(getContext());


        }
    }


}
