package com.example.fahadhd.bodybuildingtracker.Exercises;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fahadhd.bodybuildingtracker.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExercisesFragment extends Fragment {

    public ExercisesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.exercises_list_fragment, container, false);
    }


}
