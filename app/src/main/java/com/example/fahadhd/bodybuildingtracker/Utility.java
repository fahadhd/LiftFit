package com.example.fahadhd.bodybuildingtracker;


import android.content.Context;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.Sessions.Session;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

//Handles all helper methods for various classes

public class Utility {

    //Determines when the cursor should be shown or not for edit text fields.
    public static void manageEditTextCursor(final EditText iEditText, final Context context){
        iEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                iEditText.setCursorVisible(false);
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(iEditText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }
    public static Session addSession(TrackerDAO dao){
        SimpleDateFormat fmt = new SimpleDateFormat("MMMM dd");
        GregorianCalendar calendar = new GregorianCalendar();
        fmt.setCalendar(calendar);
        String dateFormatted = fmt.format(calendar.getTime());
        //TODO: Add user_weight to settings and acquire it from preferences.
        int user_weight = 185;
        long id =  dao.addSession(dateFormatted,user_weight);
        return  new Session(dateFormatted,user_weight,id);
    }

    public static void addWorkoutTask(String name, int weight, int max_sets, int max_reps,
                               TrackerDAO dao, long sessionID, int workoutCount){

        long workoutID = dao.addWorkout(sessionID,workoutCount+1,name,weight,max_sets,max_reps);
        for(int i = 1; i <= max_sets; i++){
            dao.addSet(workoutID,i,max_reps,0);
        }
    }




}
