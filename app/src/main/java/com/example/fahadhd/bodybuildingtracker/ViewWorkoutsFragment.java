package com.example.fahadhd.bodybuildingtracker;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDbHelper;

import java.util.ArrayList;

/**
 * First view a user see's when starting the app. Displays all of the user's
 * past workout and allows the ability to add a new workout which will then start another
 * activity.
 */
public class ViewWorkoutsFragment extends Fragment {

    public ViewWorkoutsFragment() {
    }

    static ArrayAdapter<String> adapter;
    static ArrayList<String> sessions = new ArrayList<String>();
    static ListView workouts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.workouts_list_fragment, container, false);
        adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.workouts_list_item,
                R.id.list_item_workout,
                new ArrayList<String>());

        workouts = (ListView) rootView.findViewById(R.id.workout_list_main);
        adapter.addAll(sessions);

        workouts.setAdapter(adapter);

        getContext().deleteDatabase(TrackerDbHelper.DATABASE_NAME);

        TrackerDAO dao = new TrackerDAO(getContext());

        long workoutKey = 0;
        for (int i = 1; i <= 10; i++) {
            long sesKey = dao.addSession("June " + i, 185);
            for (int j = 1; j <= 5; j++) {
                workoutKey = dao.addWorkout(sesKey, (j%6), "squats", 255, 5);
                for (int k = 1; k <= 4; k++) {

                    dao.addSet(workoutKey, k, 5, 0);

                }
            }
        }
        dao.updateRep(40,2,0,5);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("adapter", sessions);
    }



}