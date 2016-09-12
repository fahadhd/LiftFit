package com.example.fahadhd.bodybuildingtracker.utilities;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.exercises.Set;
import com.example.fahadhd.bodybuildingtracker.exercises.Workout;
import com.example.fahadhd.bodybuildingtracker.sessions.Session;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.Field;
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
        //Gets and stores the rows of sets from the workout in two strings
        ArrayList<Set> sets = workout.getSets();
        String row_one = "", row_two = "";
        for(int i = 0; (i < 5) && (i < sets.size()); i++){
            row_one += Integer.toString(sets.get(i).getCurrRep());
            if(i < 4){
                row_one+= " ";
            }
        }
        for(int i = 5; (i < 8) && (i < sets.size()); i++){
            row_two += Integer.toString(sets.get(i).getCurrRep());
            if(i < 7){
                row_two+= " ";
            }
        }
        /**********************SpannableStringBuilder for previews*******************/
        int start = 0, end;
        SpannableStringBuilder preview;
        /********************************Title*************************************/
        String title = WordUtils.capitalizeFully(workout.getName());
        preview = new SpannableStringBuilder(title);
        end = preview.length();
        preview.setSpan(new RelativeSizeSpan(1f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        preview.setSpan(new ForegroundColorSpan(Color.WHITE),start,end,0);
        /************************** Rows Preview *********************************/
        //Adds both sets of rows to the spannable string and sets their color
       char newLine = (row_two.length() > 0) ? '\n': 0;
        String rows = '\n'+row_one+newLine+row_two;
        preview.append(rows);
        start = end;
        end = preview.length();
        int rowColor = ContextCompat.getColor(context,R.color.blue_grey_200);
        preview.setSpan(new ForegroundColorSpan(rowColor),start,end,0);
        //Makes the rows slighter larger
        preview.setSpan(new RelativeSizeSpan(1.3f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        /************************** Unit Preview ********************************/
        String unit = getUnit(context);
        /************************** Weight Preview ********************************/
        //Converts the weight to kilogram according to user preference.
        Double weightUnit = (unit.equals("LB")) ? workout.getWeight(): workout.getWeight()*0.45359237;
        String weightString = '\n'+" "+Integer.toString(weightUnit.intValue());
        int weightColor = ContextCompat.getColor(context,R.color.orange_a400);
        preview.append(weightString);
        start = end;
        end = preview.length();

        //Adds the user weight to the preview and sets its color
        preview.setSpan(new ForegroundColorSpan(weightColor),start,end,0);

        //Adds unit after weight

        preview.append(unit);

        return preview;
    }

    public static String getUnit(Context context){
        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(context);
        String pounds = context.getString(R.string.pref_units_pounds);
        return shared_pref.getString(context.getString(R.string.pref_unit_list_key),pounds);
    }


    //Set workout previews for the top session in sessions view. No need for view holder since
    //it deals with only one item.
    public static void setTopSessionPreviews(ArrayList<Workout> workouts, View topView, Context context){
        if(workouts == null) return;
        String unit = Utility.getUnit(context);
        Workout workout_one = (workouts.size() > 0) ? workouts.get(0) : null;
        Workout workout_two = (workouts.size() > 1) ? workouts.get(1) : null;
        Workout workout_three = (workouts.size() > 2) ? workouts.get(2) : null;

        if(workout_one != null){
            ArrayList<Set> sets = workout_one.getSets();
            TextView preview_title = (TextView) topView.findViewById(R.id.preview_one_title);
            TextView preview_weight = (TextView) topView.findViewById(R.id.preview_one_weight);
            int weight = workout_one.getWeight();

            preview_title.setText(workout_one.getName());
            if(!unit.equals(context.getString(R.string.pref_units_pounds))) weight *=0.45359237;
            preview_weight.setText(Integer.toString(weight)+unit);

            preview_title.setVisibility(View.VISIBLE);
            preview_weight.setVisibility(View.VISIBLE);

            for(int i = 0; i < sets.size(); i++){
                try {
                    Class res = R.id.class;
                    Field field = res.getField("preview_one_"+(i+1));
                    int preview_id= field.getInt(null);
                    TextView preview = (TextView) topView.findViewById(preview_id);
                    preview.setText(Integer.toString(sets.get(i).getCurrRep()));
                    preview.setVisibility(View.VISIBLE);

                }
                catch (Exception e) {
                    Log.e(Utility.class.getSimpleName(), "Failure to get preview id.", e);
                }

            }
        }
        if(workout_two != null){
            ArrayList<Set> sets = workout_two.getSets();
            TextView preview_title = (TextView) topView.findViewById(R.id.preview_two_title);
            TextView preview_weight = (TextView) topView.findViewById(R.id.preview_two_weight);
            int weight = workout_two.getWeight();

            preview_title.setText(workout_two.getName());
            if(!unit.equals(context.getString(R.string.pref_units_pounds))) weight *=0.45359237;
            preview_weight.setText(Integer.toString(weight)+unit);

            preview_title.setVisibility(View.VISIBLE);
            preview_weight.setVisibility(View.VISIBLE);

            for(int i = 0; i < sets.size(); i++){
                try {
                    Class res = R.id.class;
                    Field field = res.getField("preview_two_"+(i+1));
                    int preview_id= field.getInt(null);
                    TextView preview = (TextView) topView.findViewById(preview_id);
                    preview.setText(Integer.toString(sets.get(i).getCurrRep()));
                    preview.setVisibility(View.VISIBLE);

                }
                catch (Exception e) {
                    Log.e(Utility.class.getSimpleName(), "Failure to get preview id.", e);
                }

            }
        }
        if(workout_three != null){
            ArrayList<Set> sets = workout_three.getSets();
            TextView preview_title = (TextView) topView.findViewById(R.id.preview_three_title);
            TextView preview_weight = (TextView) topView.findViewById(R.id.preview_three_weight);
            int weight = workout_three.getWeight();

            preview_title.setText(workout_three.getName());
            if(!unit.equals(context.getString(R.string.pref_units_pounds))) weight *=0.45359237;
            preview_weight.setText(Integer.toString(weight)+unit);

            preview_title.setVisibility(View.VISIBLE);
            preview_weight.setVisibility(View.VISIBLE);

            for(int i = 0; i < sets.size(); i++){
                try {
                    Class res = R.id.class;
                    Field field = res.getField("preview_three_"+(i+1));
                    int preview_id= field.getInt(null);
                    TextView preview = (TextView) topView.findViewById(preview_id);
                    preview.setText(Integer.toString(sets.get(i).getCurrRep()));
                    preview.setVisibility(View.VISIBLE);

                }
                catch (Exception e) {
                    Log.e(Utility.class.getSimpleName(), "Failure to get preview id.", e);
                }

            }
        }
    }



}
