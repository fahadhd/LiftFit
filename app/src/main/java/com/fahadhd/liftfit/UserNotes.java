package com.fahadhd.liftfit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

import com.fahadhd.liftfit.R;
import com.fahadhd.liftfit.data.TrackerDAO;
import com.fahadhd.liftfit.exercises.ExerciseActivity;

public class UserNotes extends AppCompatActivity {
    public static final String TAG = UserNotes.class.getSimpleName();
    EditText noteView;
    long sessionID;
    TrackerDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notes);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.action_notes));
        }

        TrackerApplication application = (TrackerApplication) getApplication();

        dao = application.getDatabase();

        noteView = (EditText) findViewById(R.id.user_notes);

        Intent exerciseIntent = getIntent();

        if(exerciseIntent != null){
            if(exerciseIntent.hasExtra(ExerciseActivity.SESSION_ID))
            sessionID = exerciseIntent.getLongExtra(ExerciseActivity.SESSION_ID,-1);

            if(exerciseIntent.hasExtra(ExerciseActivity.SESSION_NOTES)){
                noteView.setText(exerciseIntent.getStringExtra(ExerciseActivity.SESSION_NOTES));
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                new UpdateNotes().execute(noteView.getText().toString());
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class UpdateNotes extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            dao.updateNotes(params[0],sessionID);
            return null;
        }
    }


}
