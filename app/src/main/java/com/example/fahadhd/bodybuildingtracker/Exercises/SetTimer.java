package com.example.fahadhd.bodybuildingtracker.exercises;


import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.R;

/**Controls the timer for each set during a workout. Timer duration depends on user preference.**/
public class SetTimer implements Runnable {
    Handler handler;
    TextView timerView;
    View exerciseView, snackView;
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
        secs = secs % 60;
        timerView.setText(Integer.toString(minutes) + ":" + String.format("%02d", secs));
        handler.postDelayed(this, 1000);
    }

    public Snackbar initCustomSnackbar(){
        final Snackbar snackbar = Snackbar.make(exerciseView, "", Snackbar.LENGTH_LONG);
        LayoutInflater inflater =  exerciseActivity.getLayoutInflater();
        /**** Customize snackbar view with my own.*****/

        this.snackView = inflater.inflate(R.layout.my_snackbar, null);
        this.timerView = (TextView) snackView.findViewById(R.id.timer);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(Color.RED);
        timerView.setText("0:00");
        layout.addView(snackView, 0);
        /*********************************************/

        /**Dismiss snackbar when user presses the X button.**/
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                SetTimer.this.resetTimer();
            }
        });
        snackbar.setActionTextColor(Color.WHITE);

        return snackbar;
    }
}
