package com.fahadhd.liftfit.exercises;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.fahadhd.liftfit.data.TrackerDAO;

import java.util.List;

public class ExerciseLoader extends AsyncTaskLoader<List<Workout>> {
    List<Workout> cachedData;
    TrackerDAO dao;
    long sessionID;

    public ExerciseLoader(Context context, TrackerDAO dao, long sessionID){
        super(context);
        this.dao = dao;
        this.sessionID = sessionID;
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
        return dao.getWorkouts(sessionID,false);
    }

    @Override
    public void deliverResult(List<Workout> data) {
        cachedData = data;
        super.deliverResult(cachedData);
    }
}
