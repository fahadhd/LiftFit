package com.example.fahadhd.bodybuildingtracker;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;


import com.example.fahadhd.bodybuildingtracker.Exercises.Workout;
import com.example.fahadhd.bodybuildingtracker.Sessions.Session;
import com.example.fahadhd.bodybuildingtracker.Sessions.SessionAdapter;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDbHelper;

import java.util.ArrayList;

public class TrackerApplication extends Application {
    TrackerDAO dao;
    ArrayList<Session> sessions;
    public static int currSessionPosition;

    @Override
    public void onCreate() {
        super.onCreate();
        //this.deleteDatabase(TrackerDbHelper.DATABASE_NAME);
        dao = new TrackerDAO(getApplicationContext());
        sessions = new ArrayList<>();
        currSessionPosition = 0;
    }

    public TrackerDAO getDatabase(){
        return dao;
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public int getCurrSessionPosition() {
        return currSessionPosition;
    }
}
