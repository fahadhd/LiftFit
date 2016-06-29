package com.example.fahadhd.bodybuildingtracker;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.util.List;

public class SessionLoader extends AsyncTaskLoader<List<String>> {
    List<String> cachedData
            ;
    public SessionLoader(Context context) {
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
    //Returns a list of all sessions in tracker.db
    public List<String> loadInBackground() {
        TrackerDAO dao  = new TrackerDAO(getContext());

        return dao.getSessions();
    }

    @Override
    public void deliverResult(List<String> data) {
        cachedData = data;
        super.deliverResult(data);
    }
}
