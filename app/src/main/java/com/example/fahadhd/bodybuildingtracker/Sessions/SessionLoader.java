package com.example.fahadhd.bodybuildingtracker.Sessions;

import android.content.Context;

import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.util.List;

public class SessionLoader extends android.support.v4.content.AsyncTaskLoader<List<Session>> {
    List<Session> cachedData;
    TrackerDAO dao;

    public SessionLoader(Context context,TrackerDAO dao) {
        super(context);
        this.dao = dao;
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
    public List<Session> loadInBackground() {

        return  this.dao.getSessions();
    }

    @Override
    public void deliverResult(List<Session> data) {
        cachedData = data;
        super.deliverResult(cachedData);
    }
}
