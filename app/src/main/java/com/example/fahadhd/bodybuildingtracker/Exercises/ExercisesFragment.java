package com.example.fahadhd.bodybuildingtracker.Exercises;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.Sessions.ViewSessionsFragment;
import com.example.fahadhd.bodybuildingtracker.Sessions.Session;


public class ExercisesFragment extends Fragment {
    Session currentSession;


    public ExercisesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview =  inflater.inflate(R.layout.exercises_list_fragment, container, false);
        Intent sessionIntent = getActivity().getIntent();
        if(sessionIntent != null && sessionIntent.hasExtra(ViewSessionsFragment.INTENT_KEY)){

            //currentSession= (Session)sessionIntent.getSerializableExtra
                   // (ViewSessionsFragment.INTENT_KEY);
            setExistingWorkout(currentSession);
        }else{
            //getActivity().setTitle("Today's Workout");

            //getNewSessionInfo();
        }
        return rootview;
    }

   public void setExistingWorkout(Session session){
       String title = "Workout for "+session.getDate()+"\n"+
                      "Session "+session.getSessionId();
       //getActivity().setTitle(session.toString());

    }

    public void setNewWorkout(){

    }

}
