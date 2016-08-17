package com.example.fahadhd.bodybuildingtracker.exercises;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.utilities.Constants;


public class TimerService extends Service {
    private static String TAG = TimerService.class.getSimpleName();
    public IBinder mBinder = new MyBinder();
    Intent timerIntent = new Intent(Constants.TIMER.TIMER_RUNNING);
    long currentTimer,duration,sessionID = -1;
    long timeSinceLastOn, elapsedTimeSinceOff;
    public String message;
    boolean timerUp;
    Handler handler = new Handler();


    @Override
    public void onCreate() {
        super.onCreate();
        currentTimer = elapsedTimeSinceOff = 0L;
        duration = 60000;
        timerUp = false;
        message = "Rest a bit";
        timeSinceLastOn = SystemClock.elapsedRealtime();

        /**Broadcast receiver to check if the screen is on **/
        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(broadcastReceiver, screenStateFilter);
        /***************************************************/

        handler.postDelayed(timerThread,0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            if (intent.getAction().equals(Constants.ACTION.START_FOREGROUND_ACTION)) {
                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, createTimerNotification());
                if(intent.hasExtra(Constants.GENERAL.SESSION_ID))
                    sessionID = intent.getLongExtra(Constants.GENERAL.SESSION_ID,0);
                if(intent.hasExtra(Constants.TIMER.TIMER_MSG))
                    message = intent.getStringExtra(Constants.TIMER.TIMER_MSG);
            }
            else if (intent.getAction().equals(Constants.ACTION.STOP_FOREGROUND_ACTION)) {
                stopForeground(true);
                stopSelf();
            }
        }
        return START_STICKY;
    }

    public Runnable timerThread = new Runnable() {
        @Override
        public void run() {
            if(!timerUp && currentTimer >= duration){
                timerUp = true;
                message = "Rest time is over!";
            }
            currentTimer += 1000;
            broadCastTimer();
            handler.postDelayed(this,1000);
        }
    };
    public void broadCastTimer(){
        Log.v(TAG,"Timer is running. Current tick is: "+currentTimer);
        sendBroadcast(timerIntent);
    }
    public long getTimer(){
        return currentTimer;
    }

    public boolean isTimerUp() {
        return timerUp;
    }

    public String getMessage(){
        return message;
    }

    public void resetTimer(String message){
        Log.v(TAG,"Resetting Timer");
        currentTimer = 0L;
        timerUp = false;
        this.message = message;
    }

    public long getSessionID(){
        return (sessionID != -1) ? sessionID : -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"Timer service has stopped");
        unregisterReceiver(broadcastReceiver);
        timerIntent.setAction(Constants.TIMER.TIMER_OFF);
        sendBroadcast(timerIntent);
        handler.removeCallbacks(timerThread);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
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
                Log.d(TAG," screen was off and updating current time by"+elapsedTimeSinceOff);
                currentTimer += elapsedTimeSinceOff;
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
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.START_NOTIFICATION_ACTION);
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