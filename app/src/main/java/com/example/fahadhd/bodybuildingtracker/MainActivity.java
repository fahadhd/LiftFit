package com.example.fahadhd.bodybuildingtracker;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fahadhd.bodybuildingtracker.exercises.ExerciseActivity;
import com.example.fahadhd.bodybuildingtracker.exercises.Workout;
import com.example.fahadhd.bodybuildingtracker.sessions.Session;
import com.example.fahadhd.bodybuildingtracker.sessions.SessionsFragment;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.GregorianCalendar;

/*Front view of app with a floating action button to add a exercise at the top*/
public class MainActivity extends AppCompatActivity {

   SessionsFragment sessionsFragment;
    ArrayList<Session> sessions;
    //Database action object to query sqlite database tracker.db
    TrackerDAO dao;

    public static final String ADD_TASK = "Add_Session";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("");
        }


        TrackerApplication application  = (TrackerApplication)this.getApplication();
        dao = application.getDatabase();
        sessions = application.getSessions();

        sessionsFragment =  ((SessionsFragment)getSupportFragmentManager()
                .findFragmentById(R.id.sessions_fragment));

        FloatingActionButton addTask = (FloatingActionButton) findViewById(R.id.session_button);
        assert addTask != null;
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Adds a session to cached data
                Session newSession = addSession();
                Intent exercise = new Intent(MainActivity.this, ExerciseActivity.class).
                        putExtra(ADD_TASK, newSession);
                //Adds a new session in a background thread
                new AddSession().execute(newSession);
                startActivity(exercise);
            }
        });

    }

    public Session addSession(){
        SimpleDateFormat fmt = new SimpleDateFormat("MMM dd");
        GregorianCalendar calendar = new GregorianCalendar();
        fmt.setCalendar(calendar);
        String dateFormatted = fmt.format(calendar.getTime());
        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(this);

        int user_weight = Integer.parseInt(shared_pref.getString(getString
                (R.string.pref_user_weight_key),getString
                (R.string.pref_default_user_weight)));
        //// TODO: 8/28/2016 can add template automatically here depending on shared pref instead of 'none'
        Session session = new Session(dateFormatted,user_weight,sessions.size()+1,new ArrayList<Workout>(),"None");
        sessions.add(0,session);
        return session;
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

    public class AddSession extends AsyncTask<Session,Void,Void>{

        @Override
        protected Void doInBackground(Session... params) {
            Session session = params[0];
            dao.addSession(session.getDate(),session.getWeight());
            return null;
        }
    }


}
