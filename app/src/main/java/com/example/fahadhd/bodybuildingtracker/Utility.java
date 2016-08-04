package com.example.fahadhd.bodybuildingtracker;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.Exercises.ExerciseActivity;
import com.example.fahadhd.bodybuildingtracker.Exercises.Set;
import com.example.fahadhd.bodybuildingtracker.Exercises.WorkoutViewHolder;
import com.example.fahadhd.bodybuildingtracker.Exercises.Workout;
import com.example.fahadhd.bodybuildingtracker.Sessions.Session;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public static Session addSession(TrackerDAO dao, MainActivity activity){
        SimpleDateFormat fmt = new SimpleDateFormat("MMM dd");
        GregorianCalendar calendar = new GregorianCalendar();
        fmt.setCalendar(calendar);
        String dateFormatted = fmt.format(calendar.getTime());
        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(activity);
        int user_weight = Integer.parseInt(shared_pref.getString(activity.getString
                (R.string.pref_user_weight_key),activity.getString
                (R.string.pref_default_user_weight)));

        long id =  dao.addSession(dateFormatted,user_weight);
        return  new Session(dateFormatted,user_weight,id,null);
    }



    //Returns true if all the sets were started in the current workout.
    public static boolean allSetsStarted(Workout workout){
        for(Set set: workout.getSets()){
            if (set.getCurrRep() == 0){
                return false;
            }
        }
        return true;
    }

    //Returns true if all the sets were finished in the current workout.
    public static boolean allSetsFinished(Workout workout){
        for(Set set: workout.getSets()){
            if (set.getCurrRep() != workout.getMaxReps()){
                return false;
            }
        }
        return true;
    }

    public static String[] previewTextHelper(Workout workout){
        ArrayList<Set> sets = workout.getSets();
        String row_one = "", row_two = "";
        for(int i = 0; (i <= 4) && (i < sets.size()); i++){
            row_one += Integer.toString(sets.get(i).getCurrRep());
        }
        for(int i = 5; (i <= 7) && (i < sets.size()); i++){
            row_two += Integer.toString(sets.get(i).getCurrRep());
        }
        return new String[] {row_one,row_two};
    }


}
