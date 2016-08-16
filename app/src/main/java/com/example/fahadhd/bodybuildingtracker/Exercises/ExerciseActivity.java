package com.example.fahadhd.bodybuildingtracker.exercises;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.service.TimerService;
import com.example.fahadhd.bodybuildingtracker.sessions.Session;
import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;
import com.example.fahadhd.bodybuildingtracker.utilities.Constants;
import com.example.fahadhd.bodybuildingtracker.utilities.Utility;

import java.util.ArrayList;

//TODO: clean up classy kind of messy
public class ExerciseActivity extends AppCompatActivity implements WorkoutDialog.Communicator {
    ExercisesFragment exercisesFragment;
    TrackerDAO dao;
    public static long sessionID;
    ArrayList<Workout> workouts;
    SetTimer setTimer;
    String name;
    TrackerApplication application;
    /***********Snackbar variables**************/
    View mySnackView;
    public static TextView timerView;
    Snackbar mySnackBar;
    Intent timerService;
    long currentTime = 0L,duration = 4000;
    boolean snackBarOn = false, receiverRegistered = false;
    String serviceMessage;
    /******************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        application  = (TrackerApplication)this.getApplication();
        exercisesFragment = (ExercisesFragment) getSupportFragmentManager().
                findFragmentById(R.id.exercises_fragment);

        sessionID = exercisesFragment.sessionID;
        timerService = new Intent(this, TimerService.class);
        mySnackView = getLayoutInflater().inflate(R.layout.my_snackbar, null);
        timerView = (TextView) mySnackView.findViewById(R.id.timer);

        /**Creates a new exercise loader if one doesn't exist or refreshes the data if one does exist.**/
        if (this.getSupportLoaderManager().getLoader(R.id.exercise_loader_id) == null) {
            this.getSupportLoaderManager().initLoader(R.id.exercise_loader_id, null, exercisesFragment);
        } else {
            this.getSupportLoaderManager().restartLoader(R.id.exercise_loader_id, null, exercisesFragment);
        }

        /**Button in charge of adding workouts to the list_view. First displays a dialog for input**/
        FloatingActionButton addExercise = (FloatingActionButton) findViewById(R.id.add_exercise);
        if (addExercise != null) {
            addExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExerciseActivity.this.setTimer.dismissSnackBar();
                    WorkoutDialog dialog = new WorkoutDialog();
                    dialog.show(getFragmentManager(), "WorkoutDialog");
                }
            });
        }
        /**Initializing timer for sets**/
        setTimer = new SetTimer(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        //Register receiver if service is running
        if(isMyServiceRunning(TimerService.class))
            registerTimerBroadcasts();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onStop() {
        super.onStop();
        if(receiverRegistered) unregisterReceiver(broadcastReceiver); receiverRegistered = false;
    }



    public SetTimer getSetTimer(){
        return this.setTimer;
    }

    @Override
    public void getWorkoutInfo(String name, int weight, int max_sets, int max_reps) {
        dao = exercisesFragment.dao;
        workouts = exercisesFragment.workouts;
        this.name = name;
        new AddWorkoutTask().execute(weight, max_sets, max_reps);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                exercisesFragment.refreshSessionData();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }



    /*************** Add workout data in background thread via async task*****************/
    public class AddWorkoutTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int weight = params[0];
            int max_sets = params[1];
            int max_reps = params[2];
            addWorkoutTask(name, weight, max_sets, max_reps);
            //Restart loader so it updates the new data to the list.
            ExerciseActivity.this.getSupportLoaderManager().restartLoader(R.id.exercise_loader_id, null, exercisesFragment);
            return null;
        }

        public void addWorkoutTask(String name, int weight, int max_sets, int max_reps) {
            long workoutID = dao.addWorkout(sessionID, workouts.size() + 1, name, weight, max_sets, max_reps);
            dao.addSets(workoutID, max_sets);
        }
    }


    /****************************** Timer Snackbar Services *****************************************************/

    /**************************** BroadCast Receiver in charge of snackbar counter *******************/
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.ACTION.BROADCAST_ACTION_OFF) && mySnackBar != null){
                if(mySnackBar.isShown()) mySnackBar.dismiss();
                return;
            }
            if(intent.hasExtra(Constants.TIMER.TIMER_ID) && intent.getLongExtra(Constants.TIMER.TIMER_ID,0L) != sessionID){
                unregisterReceiver(broadcastReceiver);
                receiverRegistered = false;
                return;
            }
            /**Starting the snackbar back up if it was dismissed before timer was up**/
            if(serviceMessage == null && intent.hasExtra(Constants.TIMER.TIMER_MSG)){
                serviceMessage = intent.getStringExtra(Constants.TIMER.TIMER_MSG);
            }
           showSnackbar();

            //Updating the timer in snackbar view
            updateUI(intent);
        }
    };

    public void startTimerService(String message){
        //Dismiss snackbar and stop service before starting a new one
        stopTimerService();
        serviceMessage = message;
        showSnackbar();

        timerService.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        timerService.putExtra(Constants.TIMER.DURATION,duration);
        timerService.putExtra(Constants.TIMER.TIMER_ID,sessionID);
        timerService.putExtra(Constants.TIMER.TIMER_MSG,serviceMessage);
        startService(timerService);
        registerTimerBroadcasts();
    }

    public void stopTimerService(){
        if(snackBarOn) mySnackBar.dismiss(); snackBarOn = false;

        if(isMyServiceRunning(TimerService.class)) {
            timerService.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            startService(timerService);
            unregisterReceiver(broadcastReceiver);
            receiverRegistered = false;
        }
    }

    //Receives the timer from the service and updates the UI
    public boolean updateUI(Intent intent){
        if(!intent.hasExtra(Constants.TIMER.CURRENT_TIME)) return false;

        this.currentTime = intent.getLongExtra(Constants.TIMER.CURRENT_TIME, 0L);

        if(this.currentTime == duration) return false;


        int secs = (int) (currentTime / 1000);
        int minutes = secs / 60;

        timerView.setText(Integer.toString(minutes) + ":" + String.format("%02d", secs%60));
        return true;
    }

    public  boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public Snackbar initCustomSnackbar(String message){
        View exerciseView;
        exerciseView = findViewById(R.id.exercises_list_main);
        if(exerciseView == null) return  null;
        final Snackbar snackbar = Snackbar.make(exerciseView, message, Snackbar.LENGTH_LONG);

        /**** Customizing snackbar view with my own.*****/
        LayoutInflater inflater =  getLayoutInflater();
        mySnackView = inflater.inflate(R.layout.my_snackbar, null);
        timerView = (TextView) mySnackView.findViewById(R.id.timer);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(Color.GRAY);
        TextView snackbarText = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackbarText.setTextSize(14f);
        //layout basically works like a list where you can add views at the top and remove them.
        layout.addView(mySnackView, 0);
        /*********************************************/

        /////Makes the action button width smaller.////
        snackbar.setActionTextColor(Color.WHITE);
        Button action = (Button) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
        ViewGroup.LayoutParams params= action.getLayoutParams();
        params.width= 100;
        action.setLayoutParams(params);
        //////////////////////////////////////////////

        /**Dismisses snackbar and stop service when user presses the X button.**/
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimerService();
            }
        });

        return snackbar;
    }

    public void registerTimerBroadcasts(){
        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Constants.ACTION.BROADCAST_ACTION);
        screenStateFilter.addAction(Constants.ACTION.BROADCAST_ACTION_OFF);
        registerReceiver(broadcastReceiver, screenStateFilter);

        receiverRegistered = true;
    }

    public void showSnackbar(){
        if(!snackBarOn){
            mySnackBar = initCustomSnackbar(serviceMessage);
            snackBarOn = true;
            mySnackBar.setDuration((int)duration*5);
            mySnackBar.show();
        }
    }

    /************************** Timer Services End *******************************/


}
