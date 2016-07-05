package com.example.fahadhd.bodybuildingtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.fahadhd.bodybuildingtracker.Sessions.Session;

import java.util.ArrayList;

//Tracker data access object to edit and add data to the tracker database.
//TODO: Create an asynctask when opening the database if needed.
public class TrackerDAO {
    public static final String TAG = TrackerDAO.class.getSimpleName();
    private TrackerDbHelper mDbHelper;
    private Context mContext;
    public SQLiteDatabase db;

    public TrackerDAO(Context context){
        this.mContext = context;
        mDbHelper = new TrackerDbHelper(context);
        try{
            open();
        }
        catch(SQLiteException e){
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLiteException{
            db = mDbHelper.getReadableDatabase();
    }

    public void close(){
        mDbHelper.close();
    }


    public long addSession(String date, int weight){
        ContentValues values  = new ContentValues();
        values.put(TrackerDbHelper.SessionEntry.COLUMN_DATE,date);
        values.put(TrackerDbHelper.SessionEntry.COLUMN_USER_WEIGHT, weight);

        long session_id =  db.insert(TrackerDbHelper.SessionEntry.TABLE_NAME,null,values);
        return session_id;


    }
    //Add a workout to a current session
    public long addWorkout(long sesKey, int workoutNum, String name, int weight, int maxSets ){
        ContentValues values  = new ContentValues();
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_SES_KEY, sesKey);
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_WORKOUT_NUM, workoutNum);
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_NAME, name);
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_WEIGHT, weight);
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_MAX_SETS,maxSets);


        long workout_id =  db.insert(TrackerDbHelper.WorkoutEntry.TABLE_NAME,null,values);
        return workout_id;
    }

    //Add a set to a current workout
    public long addSet(long workoutKey,int setNum, int maxReps, int currRep){
        ContentValues values  = new ContentValues();

        values.put(TrackerDbHelper.SetEntry.COLUMN_WORK_KEY, workoutKey);
        values.put(TrackerDbHelper.SetEntry.COLUMN_SET_NUM, setNum);
        values.put(TrackerDbHelper.SetEntry.COLUMN_MAX_REPS, maxReps);
        values.put(TrackerDbHelper.SetEntry.COLUMN_CURR_REP, currRep);

        long set_id = db.insert(TrackerDbHelper.SetEntry.TABLE_NAME,null,values);
        return set_id;
    }

    //Update a current rep in a set.
    public void updateRep(long workoutKey, int setNum, int currRep, int maxRep){
        String workoutKeyName = TrackerDbHelper.SetEntry.COLUMN_WORK_KEY;
        String setNumName = TrackerDbHelper.SetEntry.COLUMN_SET_NUM;
        String where = workoutKeyName+" = "+workoutKey + " AND " + setNumName+" = " +setNum;

        //If the current rep is less than the max rep increment it by one else set it to 0.
        int newRep = (currRep < maxRep) ?  currRep+1: 0  ;
        ContentValues values = new ContentValues();
        values.put(TrackerDbHelper.SetEntry.COLUMN_CURR_REP,newRep);

        db.update(TrackerDbHelper.SetEntry.TABLE_NAME,values,where,null);
    }



    public ArrayList<Session> getSessions(){
        ArrayList<Session> sessions = new ArrayList<>();
        String[] columns = {TrackerDbHelper.SessionEntry._ID,TrackerDbHelper.SessionEntry.COLUMN_DATE,
                TrackerDbHelper.SessionEntry.COLUMN_USER_WEIGHT};
        Cursor cursor = db.
                query(TrackerDbHelper.SessionEntry.TABLE_NAME,columns,null,null,null,null,null);

        while(cursor.moveToNext()){
            int dateIndex = cursor.getColumnIndex(TrackerDbHelper.SessionEntry.COLUMN_DATE);
            int weightIndex = cursor.getColumnIndex(TrackerDbHelper.SessionEntry.COLUMN_USER_WEIGHT);
            int sessionNum = cursor.getColumnIndex(TrackerDbHelper.SessionEntry._ID);

            String date = cursor.getString(dateIndex);
            int weight = cursor.getInt(weightIndex);
            long sessionId = cursor.getLong(sessionNum);
            sessions.add(new Session(date,weight,sessionId));
        }
        cursor.close();


        return sessions;
    }


}
