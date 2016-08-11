package com.example.fahadhd.bodybuildingtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fahadhd.bodybuildingtracker.exercises.ExerciseActivity;
import com.example.fahadhd.bodybuildingtracker.sessions.SessionsFragment;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

/*Front view of app with a floating action button to add a exercise at the top*/
public class MainActivity extends AppCompatActivity {

   SessionsFragment sessionsFragment;
    //Database action object to query sqlite database tracker.db
    TrackerDAO dao;

    public static final String ADD_TASK = "Add_Session";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TrackerApplication application  = (TrackerApplication)this.getApplication();
        dao = application.getDatabase();


        sessionsFragment =  ((SessionsFragment)getSupportFragmentManager()
                .findFragmentById(R.id.sessions_fragment));

        FloatingActionButton addTask = (FloatingActionButton) findViewById(R.id.session_button);
        assert addTask != null;
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent exercise = new Intent(MainActivity.this, ExerciseActivity.class).
                        putExtra(ADD_TASK,Utility.addSession(dao,MainActivity.this));
                startActivity(exercise);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
