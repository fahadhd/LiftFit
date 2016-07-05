package com.example.fahadhd.bodybuildingtracker;

import android.app.Application;
import android.widget.Toast;


import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDbHelper;

public class TrackerApplication extends Application {
    TrackerDAO dao;
    @Override
    public void onCreate() {
        super.onCreate();
        //this.deleteDatabase(TrackerDbHelper.DATABASE_NAME);
        dao = new TrackerDAO(getApplicationContext());
        // Toast.makeText(this,"APP ON_CREATE WAS CALLED",Toast.LENGTH_SHORT).show();
    }

    public TrackerDAO getDatabase(){
        return dao;
    }
}
