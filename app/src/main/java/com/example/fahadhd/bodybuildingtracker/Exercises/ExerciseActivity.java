package com.example.fahadhd.bodybuildingtracker.Exercises;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


import com.example.fahadhd.bodybuildingtracker.R;


public class ExerciseActivity extends AppCompatActivity implements WorkoutDialog.Communicator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        Toolbar toolbar = (Toolbar) findViewById(R.id.exercise_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton addExercise = (FloatingActionButton)findViewById(R.id.add_exercise);
        if (addExercise != null) {
            addExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WorkoutDialog dialog = new WorkoutDialog();
                    dialog.show(getFragmentManager(),"WorkoutDialog");
                }
            });
        }


    }

    @Override
    public void getWorkoutInfo(String name, int weight, int max_sets, int max_reps) {

    }
}
