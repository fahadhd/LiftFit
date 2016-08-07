package com.example.fahadhd.bodybuildingtracker;

import android.app.Application;
import android.widget.Toast;


import com.example.fahadhd.bodybuildingtracker.Exercises.Workout;
import com.example.fahadhd.bodybuildingtracker.Sessions.Session;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDbHelper;

import java.util.ArrayList;

public class TrackerApplication extends Application {
    TrackerDAO dao;
    ArrayList<Session> sessions;

    @Override
    public void onCreate() {
        super.onCreate();
        //this.deleteDatabase(TrackerDbHelper.DATABASE_NAME);
        dao = new TrackerDAO(getApplicationContext());
        sessions = new ArrayList<>();
        // Toast.makeText(this,"APP ON_CREATE WAS CALLED",Toast.LENGTH_SHORT).show();
    }

    public TrackerDAO getDatabase(){
        return dao;
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }
}
