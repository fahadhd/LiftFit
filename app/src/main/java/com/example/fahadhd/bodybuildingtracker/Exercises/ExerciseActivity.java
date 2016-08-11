package com.example.fahadhd.bodybuildingtracker.exercises;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;


import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.sessions.Session;
import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.util.ArrayList;

//TODO: clean up classy kind of messy
public class ExerciseActivity extends AppCompatActivity implements WorkoutDialog.Communicator {
    ExercisesFragment exercisesFragment;
    TrackerDAO dao;
    public static long sessionID;
    ArrayList<Session> sessions;
    ArrayList<Workout> workouts;
    SetTimer setTimer;
    String name;
    TrackerApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        application  = (TrackerApplication)this.getApplication();
        exercisesFragment = (ExercisesFragment) getSupportFragmentManager().
                findFragmentById(R.id.exercises_fragment);
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
    protected void onResume() {
        super.onResume();

    }

    public SetTimer getSetTimer(){
        return this.setTimer;
    }

    @Override
    public void getWorkoutInfo(String name, int weight, int max_sets, int max_reps) {

        dao = exercisesFragment.dao;
        sessionID = exercisesFragment.sessionID;
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
    /***********************************************************************************/


}
