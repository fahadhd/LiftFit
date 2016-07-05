package com.example.fahadhd.bodybuildingtracker;

import android.app.Application;


import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

public class TrackerApplication extends Application {
    TrackerDAO dao;
    @Override
    public void onCreate() {
        super.onCreate();
        dao = new TrackerDAO(getApplicationContext());
    }

    public TrackerDAO getDatabase(){
        return dao;
    }
}
