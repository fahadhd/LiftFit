package com.example.fahadhd.bodybuildingtracker.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.exercises.ExerciseActivity;
import com.example.fahadhd.bodybuildingtracker.utilities.Constants;

//Timer service which uses a handler to monitor tick rate. Also uses a broadcast receiver
//to update the timer if the device was in sleep mode.
public class TimerService extends Service {
    Intent intent;
    public static final String TAG = TimerService.class.getSimpleName();
    private final Handler handler = new Handler();
    long currentTime, duration;
    long timeSinceLastOn, elapsedTimeSinceOff;
    long timerID;
    String message;

    @Override
    public void onCreate() {
        super.onCreate();
        currentTime = duration = elapsedTimeSinceOff = 0L;
        timeSinceLastOn = SystemClock.elapsedRealtime();
        intent = new Intent(Constants.ACTION.BROADCAST_ACTION);

        /**Starting Timer here**/
        handler.removeCallbacks(timerThread);
        handler.postDelayed(timerThread,500);
        /**********************/

        /**Broadcast receiver to check if the screen is on **/
        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(broadcastReceiver, screenStateFilter);
        /***************************************************/

    }

    @Override
    /**Depending on action issued by MainActivity either puts service in foreground with duration
     * or destroys the service**/
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
                if (intent.hasExtra(Constants.TIMER.DURATION))
                    duration = intent.getLongExtra(Constants.TIMER.DURATION, 0);
                if(intent.hasExtra(Constants.TIMER.TIMER_ID))
                    timerID = intent.getLongExtra(Constants.TIMER.TIMER_ID,0);
                if(intent.hasExtra(Constants.TIMER.TIMER_MSG))
                    message = intent.getStringExtra(Constants.TIMER.TIMER_MSG);

                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, createTimerNotification());
            }
            else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
                stopForeground(true);
                stopSelf();
            }
        }
        return START_STICKY;
    }

    /**Thread the handler uses to push to message queue. This creates a timer effect.**/
    private Runnable timerThread = new Runnable() {
        @Override
        public void run() {
            if(currentTime >= duration){
                stopSelf();
                return;
            }
            currentTime += 1000;
            sendTimerInfo();
            handler.postDelayed(this,1000);
        }
    };

    /**Broadcasts the timer in which the MainActivity will receive it and update the UI**/
    private void sendTimerInfo(){
        Log.d(TAG, "timer running: tick is " + currentTime);
        intent.putExtra(Constants.TIMER.CURRENT_TIME, currentTime);
        intent.putExtra(Constants.TIMER.TIMER_ID,timerID);
        intent.putExtra(Constants.TIMER.TIMER_MSG,message);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"timer service finished");
        unregisterReceiver(broadcastReceiver);
        intent.setAction(Constants.ACTION.BROADCAST_ACTION_OFF);
        sendBroadcast(intent);
        handler.removeCallbacks(timerThread);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /******************** Broadcast Receiver To Check if Screen is on**************************************/
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.removeCallbacks(timerThread);
            /**If the screen is back on then update the timer and start it again**/
            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                Log.d(TAG,"Screen is turned on");
                elapsedTimeSinceOff = SystemClock.elapsedRealtime() - timeSinceLastOn;
                Log.d(TAG," screen was off and updating current time by "+elapsedTimeSinceOff);
                currentTime += elapsedTimeSinceOff;
                handler.postDelayed(timerThread,0);
            }
            /**Turns off the timer when the screen is off**/
            else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                Log.d(TAG,"Screen is turned off");
                timeSinceLastOn = SystemClock.elapsedRealtime();
            }
        }
    };

    /**Since this is foreground service it must have a notification**/
    private Notification createTimerNotification() {
        //TODO: Fix notification and finalize it
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent,0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Service Timer")
                .setTicker("Count up timer")
                .setContentText("timer")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        return notification;
    }
}
