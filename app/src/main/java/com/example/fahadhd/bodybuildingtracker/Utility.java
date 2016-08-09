package com.example.fahadhd.bodybuildingtracker;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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
    //Displays the workout previews in the session list view.
    public static CharSequence previewTextHelper(Workout workout, Context context){
        ArrayList<Set> sets = workout.getSets();
        String row_one = "", row_two = "";
        for(int i = 0; (i < 4) && (i < sets.size()); i++){
            row_one += Integer.toString(sets.get(i).getCurrRep());
            if(i < 4){
                row_one+= " ";
            }
        }
        for(int i = 4; (i < 9) && (i < sets.size()); i++){
            row_two += Integer.toString(sets.get(i).getCurrRep());
            if(i < 7){
                row_two+= " ";
            }
        }
        /**************************Rows Preview*********************************/
        //Adds both sets of rows to the spannable string and sets their color
        String rows = row_one+'\n'+row_two;
        int rowColor = ContextCompat.getColor(context,R.color.blue_grey_200);
        SpannableStringBuilder preview = new SpannableStringBuilder(rows);
        preview.setSpan(new ForegroundColorSpan(rowColor),0,rows.length(),0);
        //Makes the rows slighter larger
        preview.setSpan(new RelativeSizeSpan(1.4f), 0, rows.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        /**************************Unit Preview********************************/
        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(context);
        String pounds = context.getString(R.string.pref_units_pounds);
        String unit = shared_pref.getString(context.getString(R.string.pref_unit_list_key),pounds);

        /**************************Weight Preview ********************************/
        //Converts the weight to kilogram according to user preference.
        Double userWeight = (unit.equals(pounds)) ? workout.getWeight(): workout.getWeight()*0.45359237;
        String weight = '\n'+" "+Integer.toString(userWeight.intValue());
        int weightColor = ContextCompat.getColor(context,R.color.orange_a400);
        preview.append(weight);
        //Adds the user weight to the preview and sets its color
        preview.setSpan(new ForegroundColorSpan(weightColor),rows.length(),preview.length(),0);
        //Adds unit after weight
        preview.append(unit);

        return preview;
    }


}
