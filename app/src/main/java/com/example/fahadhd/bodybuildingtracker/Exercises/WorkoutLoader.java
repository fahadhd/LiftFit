package com.example.fahadhd.bodybuildingtracker.Exercises;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class WorkoutLoader extends AsyncTaskLoader<List<Workout>> {
    List<Workout> cachedData;

    public WorkoutLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if(cachedData == null){
            forceLoad();
        }
        else{
            super.deliverResult(cachedData);
        }
    }

    @Override
    public List<Workout> loadInBackground() {
        return null;
    }

    @Override
    public void deliverResult(List<Workout> data) {
        cachedData = data;
        super.deliverResult(cachedData);
    }
}
