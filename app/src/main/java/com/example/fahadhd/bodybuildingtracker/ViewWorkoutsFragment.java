package com.example.fahadhd.bodybuildingtracker;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    static ListView workouts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.workouts_list_fragment, container, false);

        workouts = (ListView)rootView.findViewById(R.id.workout_list_main);
        adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.workouts_list_item,
                R.id.list_item_workout,
                new ArrayList<String>());


        return  rootView;
}

}