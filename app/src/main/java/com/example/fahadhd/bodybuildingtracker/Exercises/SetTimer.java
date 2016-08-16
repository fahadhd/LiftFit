package com.example.fahadhd.bodybuildingtracker.exercises;


import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.R;

/**Controls the timer for each set during a workout. Timer duration depends on user preference.**/
public class SetTimer implements Runnable {
    Handler handler;
    TextView timerView;
    View exerciseView, mySnackView;
    long current_time;
    Snackbar currSnackBar;
    ExerciseActivity exerciseActivity;

    public  SetTimer(ExerciseActivity exerciseActivity){
        this.handler = new Handler();
        this.current_time = 0L;
        this.exerciseActivity = exerciseActivity;
        this.exerciseView = exerciseActivity.findViewById(R.id.exercises_list_main);
    }

    public SetTimer startTimer(String message, int duration){
        this.current_time = 0L;
        this.resetTimer();
        currSnackBar = initCustomSnackbar();
        currSnackBar.setText(message).setDuration(duration);
        handler.postDelayed(this,1000);
        currSnackBar.show();
        return this;
    }
    public SetTimer resetTimer(){
        handler.removeCallbacks(this);
        return this;
    }
    public SetTimer dismissSnackBar(){
        if(currSnackBar != null && currSnackBar.isShown()){
            currSnackBar.dismiss();
        }
        return this;
    }

    @Override
    public void run() {
        current_time += 1000;
        int secs = (int) (current_time / 1000);
        int minutes = secs / 60;

        timerView.setText(Integer.toString(minutes) + ":" + String.format("%02d", secs % 60));
        handler.postDelayed(this, 1000);
    }

    public Snackbar initCustomSnackbar(){
        final Snackbar snackbar = Snackbar.make(exerciseView, "", Snackbar.LENGTH_LONG);
        LayoutInflater inflater =  exerciseActivity.getLayoutInflater();

        /**** Customizing snackbar view with my own.*****/
        this.mySnackView = inflater.inflate(R.layout.my_snackbar,null);
        this.timerView = (TextView) mySnackView.findViewById(R.id.timer);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(Color.GRAY);
        TextView snackbarText = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackbarText.setTextSize(14f);
        //layout basically works like a list where you can add views at the top and remove them.
        layout.addView(mySnackView, 0);
        /*********************************************/

        /**Dismiss snackbar when user presses the X button.**/
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                SetTimer.this.resetTimer();
            }
        });

        //Make the action button width smaller.
        snackbar.setActionTextColor(Color.WHITE);
        Button action = (Button) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
        ViewGroup.LayoutParams params= action.getLayoutParams();
        params.width= 100;

        action.setLayoutParams(params);

        return snackbar;
    }
}
