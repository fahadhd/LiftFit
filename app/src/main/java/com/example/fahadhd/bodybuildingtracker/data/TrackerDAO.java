package com.example.fahadhd.bodybuildingtracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.fahadhd.bodybuildingtracker.exercises.Set;
import com.example.fahadhd.bodybuildingtracker.exercises.Workout;
import com.example.fahadhd.bodybuildingtracker.sessions.Session;

import java.util.ArrayList;

//Tracker data access object to edit and add data to the tracker database.
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
            Log.e(TAG, "SQLException on opening database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLiteException{
        db = mDbHelper.getReadableDatabase();
    }


    /**************************INSERTIONS TO THE DATABASE********************************/
    public long addSession(String date, int weight){
        ContentValues values  = new ContentValues();
        values.put(TrackerDbHelper.SessionEntry.COLUMN_DATE,date);
        values.put(TrackerDbHelper.SessionEntry.COLUMN_USER_WEIGHT, weight);

        return db.insert(TrackerDbHelper.SessionEntry.TABLE_NAME,null,values);
    }
    //Add a workout to a current session
    public Workout addWorkout(long sesKey, String name,
                              int weight, int maxSets, int maxReps ){
        ContentValues values  = new ContentValues();
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_SES_KEY, sesKey);
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_NAME, name);
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_WEIGHT, weight);
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_MAX_SETS,maxSets);
        values.put(TrackerDbHelper.WorkoutEntry.COLUMN_MAX_REPS, maxReps);

        long workoutId =  db.insert(TrackerDbHelper.WorkoutEntry.TABLE_NAME,null,values);
        return new Workout(sesKey, workoutId,name,weight,maxSets,maxReps,new ArrayList<Set>());

    }
    //Add all sets to a current workout
    public ArrayList<Set> addSets(long workoutKey,int maxSets){
        ArrayList<Set> sets = new ArrayList<>();
        db.beginTransaction();
        for(int i = 1; i <= maxSets; i++) {
            ContentValues values  = new ContentValues();
            values.put(TrackerDbHelper.SetEntry.COLUMN_WORK_KEY, workoutKey);
            values.put(TrackerDbHelper.SetEntry.COLUMN_SET_NUM, i);
            long setId = db.insert(TrackerDbHelper.SetEntry.TABLE_NAME,null,values);
            sets.add(new Set(setId,workoutKey,i,0));
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return sets;
    }

    public void saveWorkoutToTemplate(String templateName, Workout workout ){
        if(templateName.isEmpty()) return;
        ContentValues values = new ContentValues();
        values.put(TrackerDbHelper.TemplateEntry.COLUMN_TEMPLATE_NAME, templateName);
        values.put(TrackerDbHelper.TemplateEntry.COLUMN_WORKOUT_NAME, workout.getName());
        values.put(TrackerDbHelper.TemplateEntry.COLUMN_WEIGHT, workout.getWeight());
        values.put(TrackerDbHelper.TemplateEntry.COLUMN_MAX_SETS,workout.getMaxSets());
        values.put(TrackerDbHelper.TemplateEntry.COLUMN_MAX_REPS, workout.getMaxReps());
        db.insert(TrackerDbHelper.TemplateEntry.TABLE_NAME,null,values);
    }

    /**************************QUERIES TO THE DATABASE********************************/
    //Returns all sessions in the workout. A bit costly, only executed once when app starts.
    public ArrayList<Session> getSessions(){
        ArrayList<Session> sessions = new ArrayList<>();
        String[] columns = {TrackerDbHelper.SessionEntry._ID,TrackerDbHelper.SessionEntry.COLUMN_DATE,
                TrackerDbHelper.SessionEntry.COLUMN_USER_WEIGHT,TrackerDbHelper.SessionEntry.COLUMN_TEMPLATE_NAME};
        Cursor cursor = db.
                query(TrackerDbHelper.SessionEntry.TABLE_NAME,columns,null,null,null,null,null);

        while(cursor.moveToNext()){
            int sessionNum = cursor.getColumnIndex(TrackerDbHelper.SessionEntry._ID);
            int dateIndex = cursor.getColumnIndex(TrackerDbHelper.SessionEntry.COLUMN_DATE);
            int weightIndex = cursor.getColumnIndex(TrackerDbHelper.SessionEntry.COLUMN_USER_WEIGHT);
            int templateNameIndex = cursor.getColumnIndex(TrackerDbHelper.SessionEntry.COLUMN_TEMPLATE_NAME);

            String date = cursor.getString(dateIndex);
            int weight = cursor.getInt(weightIndex);
            long sessionId = cursor.getLong(sessionNum);
            String templateName = cursor.getString(templateNameIndex);
            sessions.add(0,new Session(date,weight,sessionId,getWorkouts(sessionId,true),templateName));
        }
        cursor.close();


        return sessions;
    }


    //Returns all workouts of a session. Returns only the first three workouts if getPreviews is true.
    //TODO: Make sure you can only add up to 8 workouts per session.
    public ArrayList<Workout> getWorkouts(long sessionID,boolean getPreviews){
        ArrayList<Workout> workouts = new ArrayList<>();
        String[] columns = {
                TrackerDbHelper.WorkoutEntry._ID,
                TrackerDbHelper.WorkoutEntry.COLUMN_NAME,
                TrackerDbHelper.WorkoutEntry.COLUMN_WEIGHT,
                TrackerDbHelper.WorkoutEntry.COLUMN_MAX_SETS,
                TrackerDbHelper.WorkoutEntry.COLUMN_MAX_REPS};

        String where = TrackerDbHelper.WorkoutEntry.COLUMN_SES_KEY+" = "+sessionID;

        Cursor cursor = db.
                query(TrackerDbHelper.WorkoutEntry.TABLE_NAME,columns,where,null,null,null,null);

        int workoutNum = (getPreviews) ? 3 : 8;
        int i = 0;

        while(i < workoutNum && cursor.moveToNext()){
            int workoutKey = cursor.getColumnIndex(TrackerDbHelper.WorkoutEntry._ID);
            int name_column = cursor.getColumnIndex(TrackerDbHelper.WorkoutEntry.COLUMN_NAME);
            int weight_column = cursor.getColumnIndex(TrackerDbHelper.WorkoutEntry.COLUMN_WEIGHT);
            int max_sets_column = cursor.getColumnIndex(TrackerDbHelper.WorkoutEntry.COLUMN_MAX_SETS);
            int max_reps_column = cursor.getColumnIndex(TrackerDbHelper.WorkoutEntry.COLUMN_MAX_REPS);

            long workoutId = cursor.getLong(workoutKey);
            String name = cursor.getString(name_column);
            int weight = cursor.getInt(weight_column);
            int maxSets = cursor.getInt(max_sets_column);
            int maxReps = cursor.getInt(max_reps_column);

            ArrayList<Set> sets = getSets(workoutId);
            Workout workout = new Workout(sessionID,workoutId,name,
                    weight,maxSets,maxReps,sets);

            workouts.add(workout);
            i++;
        }
        cursor.close();
        return workouts;
    }

    //Returns all the sets in a current workout
    public ArrayList<Set> getSets(long workoutID){
        ArrayList<Set> sets = new ArrayList<>();
        String[] columns = {
                TrackerDbHelper.SetEntry._ID,
                TrackerDbHelper.SetEntry.COLUMN_SET_NUM,
                TrackerDbHelper.SetEntry.COLUMN_CURR_REP
        };

        String where = TrackerDbHelper.SetEntry.COLUMN_WORK_KEY+" = "+workoutID;

        Cursor cursor = db.query(
                TrackerDbHelper.SetEntry.TABLE_NAME,
                columns,where,
                null,null,null,null);

        while(cursor.moveToNext()){
            int setKey = cursor.getColumnIndex(TrackerDbHelper.SetEntry._ID);
            int orderNumKey = cursor.getColumnIndex(TrackerDbHelper.SetEntry.COLUMN_SET_NUM);
            int curRepKey = cursor.getColumnIndex(TrackerDbHelper.SetEntry.COLUMN_CURR_REP);

            long setID = cursor.getLong(setKey);
            int orderNum = cursor.getInt(orderNumKey);
            int currRep = cursor.getInt(curRepKey);

            sets.add(new Set(setID,workoutID,orderNum,currRep));
        }
        cursor.close();
        return sets;
    }

    //Loads a template in a current session
    public ArrayList<Workout> loadTemplate(String templateName, long sessionID){
        if(templateName.isEmpty()) return null;
        String[] columns = {
                TrackerDbHelper.TemplateEntry.COLUMN_WORKOUT_NAME,
                TrackerDbHelper.TemplateEntry.COLUMN_WEIGHT,
                TrackerDbHelper.TemplateEntry.COLUMN_MAX_SETS,
                TrackerDbHelper.TemplateEntry.COLUMN_MAX_REPS};
        String where = TrackerDbHelper.TemplateEntry.COLUMN_TEMPLATE_NAME + " = ?";
        Cursor cursor = db.query(TrackerDbHelper.TemplateEntry.TABLE_NAME,columns,where,
                new String[]{templateName},null,null,null);
        ArrayList<Workout> workouts = new ArrayList<>();
        while(cursor.moveToNext()){
            int name_column = cursor.getColumnIndex(TrackerDbHelper.WorkoutEntry.COLUMN_NAME);
            int weight_column = cursor.getColumnIndex(TrackerDbHelper.WorkoutEntry.COLUMN_WEIGHT);
            int max_sets_column = cursor.getColumnIndex(TrackerDbHelper.WorkoutEntry.COLUMN_MAX_SETS);
            int max_reps_column = cursor.getColumnIndex(TrackerDbHelper.WorkoutEntry.COLUMN_MAX_REPS);

            String name = cursor.getString(name_column);
            int weight = cursor.getInt(weight_column);
            int maxSets = cursor.getInt(max_sets_column);
            int maxReps = cursor.getInt(max_reps_column);
            Workout newWorkout = addWorkout(sessionID,name,weight,maxSets,maxReps);
            addSets(newWorkout.getWorkoutID(),maxSets);
            workouts.add(newWorkout);
        }
        cursor.close();
        return workouts;
    }



    /**************************UPDATES TO THE DATABASE********************************/
    //Update a current rep in a set. Returns the new current rep of a set.
    public int updateRep(long workoutID, int setNum, int currRep, int maxRep){
        String workoutIDColumn = TrackerDbHelper.SetEntry.COLUMN_WORK_KEY;
        String setNumName = TrackerDbHelper.SetEntry.COLUMN_SET_NUM;
        String where = workoutIDColumn+" = "+workoutID + " AND " + setNumName+" = " +setNum;

        //If the current rep is less than the max rep increment it by one else set it to 0.
        int newRep = (currRep <= 0) ?  maxRep: currRep-1  ;
        ContentValues values = new ContentValues();
        values.put(TrackerDbHelper.SetEntry.COLUMN_CURR_REP,newRep);

        db.update(TrackerDbHelper.SetEntry.TABLE_NAME,values,where,null);
        return newRep;
    }
    //Updates workout (if it needs to be) in the database and returns a new and updated workout object
    public void updateWorkout(Workout oldWorkout, Workout updatedWorkout){
        String workoutIDColumn = TrackerDbHelper.WorkoutEntry._ID;
        String where = workoutIDColumn+" = "+oldWorkout.getWorkoutID();
        String name= updatedWorkout.getName();
        int weight = updatedWorkout.getWeight();
        int maxSet  = updatedWorkout.getMaxSets();
        int maxRep = updatedWorkout.getMaxReps();
        ContentValues values = new ContentValues();
        if(!name.equals(oldWorkout.getName())) values.put(TrackerDbHelper.WorkoutEntry.COLUMN_NAME,name);
        if(weight != oldWorkout.getWeight()) values.put(TrackerDbHelper.WorkoutEntry.COLUMN_WEIGHT,weight);
        if(maxSet != oldWorkout.getMaxSets()) values.put(TrackerDbHelper.WorkoutEntry.COLUMN_MAX_SETS,maxSet);
        if(maxRep != oldWorkout.getMaxReps()) values.put(TrackerDbHelper.WorkoutEntry.COLUMN_MAX_REPS,maxRep);
        deleteSets(oldWorkout.getWorkoutID());
        db.update(TrackerDbHelper.WorkoutEntry.TABLE_NAME,values,where,null);
    }

    public void updateTemplateToSession(String templateName, long sessionID){
        ContentValues values = new ContentValues();
        String where = TrackerDbHelper.SessionEntry._ID + " = "+sessionID;
        values.put(TrackerDbHelper.SessionEntry.COLUMN_TEMPLATE_NAME,templateName);
        db.update(TrackerDbHelper.SessionEntry.TABLE_NAME,values,where,null);
    }


    /*************** Queries to delete ****************/

    //deletes all workouts in a session
    public void deleteAllWorkouts(long sessionID){
        String where = TrackerDbHelper.WorkoutEntry.COLUMN_SES_KEY + " = "+ sessionID;
        db.delete(TrackerDbHelper.WorkoutEntry.TABLE_NAME,where,null);
    }


    public void deleteWorkout(long workoutID){
        String where = TrackerDbHelper.WorkoutEntry._ID + " = "+ workoutID;
        db.delete(TrackerDbHelper.WorkoutEntry.TABLE_NAME,where,null);
    }

    public void deleteSets(long workoutID){
        String where = TrackerDbHelper.SetEntry.COLUMN_WORK_KEY + " = "+ workoutID;
        db.delete(TrackerDbHelper.SetEntry.TABLE_NAME,where,null);
    }

    public void deleteTemplate(String templateName){
        String where = TrackerDbHelper.TemplateEntry.COLUMN_TEMPLATE_NAME + " = ?";
        db.delete(TrackerDbHelper.TemplateEntry.TABLE_NAME,where,new String[]{templateName});

        where = TrackerDbHelper.SessionEntry.COLUMN_TEMPLATE_NAME + " = ?";
        ContentValues values = new ContentValues();
        values.put(TrackerDbHelper.SessionEntry.COLUMN_TEMPLATE_NAME,"None");
        db.update(TrackerDbHelper.SessionEntry.TABLE_NAME,values,where,new String[]{templateName});
    }


    /****************************** Check db contents ****************************/

    public boolean isSessionsEmpty(){
        boolean isEmpty = true;
        String sessionTable = TrackerDbHelper.SessionEntry.TABLE_NAME;
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM "+sessionTable, null);
        if (cur != null && cur.moveToFirst()) {
            isEmpty = (cur.getInt (0) == 0);
        }
        if(cur != null) cur.close();
        return isEmpty;
    }

}
