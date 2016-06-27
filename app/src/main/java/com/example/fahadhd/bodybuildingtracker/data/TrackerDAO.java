package com.example.fahadhd.bodybuildingtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


public class TrackerDAO {
    public static final String TAG = TrackerDAO.class.getSimpleName();
    private TrackerDbHelper mDbHelper;
    private Context mContext;
    private SQLiteDatabase db;


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
    public long addSession(String date, String weight){
        ContentValues values  = new ContentValues();
        values.put(TrackerDbHelper.SessionEntry.COLUMN_DATE,date);
        values.put(TrackerDbHelper.SessionEntry.COLUMN_USER_WEIGHT, weight);

        long session_id =  db.insert(TrackerDbHelper.SessionEntry.TABLE_NAME,null,values);
        return session_id;


    }
    public long addWorkout(String sessionId, int workoutNum, String name, int weight, int maxSets ){
        ContentValues values  = new ContentValues();
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_SES_KEY, sessionId);
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_WORKOUT_NUM, workoutNum);
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_NAME, name);
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_WEIGHT, weight);
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_MAX_SETS,maxSets);


        long workout_id =  db.insert(TrackerDbHelper.WorkoutEntry.TABLE_NAME,null,values);
        return workout_id;
    }

    public long addSet(String workoutId,int setNum, int maxReps, int currRep){
        ContentValues values  = new ContentValues();

        values.put(TrackerDbHelper.SetEntry.COLUMN_WORK_KEY, workoutId);
        values.put(TrackerDbHelper.SetEntry.COLUMN_SET_NUM, setNum);
        values.put(TrackerDbHelper.SetEntry.COLUMN_MAX_REPS, maxReps);
        values.put(TrackerDbHelper.SetEntry.COLUMN_CURR_REP, currRep);

        long set_id = db.insert(TrackerDbHelper.SetEntry.TABLE_NAME,null,values);
        return set_id;
    }

    public int updateSet(int rowId, int setNum){
        return -1;

    }
}
