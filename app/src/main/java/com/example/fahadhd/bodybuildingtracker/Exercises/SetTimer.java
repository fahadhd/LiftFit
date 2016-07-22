package com.example.fahadhd.bodybuildingtracker.Exercises;


import android.os.Handler;
import android.widget.TextView;

public class SetTimer implements Runnable {
    Handler handler;
    TextView timerView;
    long current_time;

    public  SetTimer(){
        this.handler = new Handler();
        current_time = 0L;
    }

    public void startTimer(TextView timerView){
        this.timerView = timerView;
        this.current_time = 0L;
        handler.postDelayed(this,1000);
    }
    public void cancelTimer(TextView timerView){
        this.timerView = timerView;
        handler.removeCallbacks(this);
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


}
