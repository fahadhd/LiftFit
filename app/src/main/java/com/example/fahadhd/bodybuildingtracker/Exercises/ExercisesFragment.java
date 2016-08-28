package com.example.fahadhd.bodybuildingtracker.exercises;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.sessions.SessionsFragment;
import com.example.fahadhd.bodybuildingtracker.sessions.Session;
import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


public class ExercisesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Workout>> {
    public static final String TAG = ExercisesFragment.class.getSimpleName();
    TrackerDAO dao;
    ExerciseAdapter adapter;
    Session currentSession;
    long sessionID;
    int position;
    ArrayList<Session> sessions;
    ListView exerciseListView;
    Menu exerciseMenu;
    FloatingActionButton fabExercise;
    ImageButton template_A, template_B;
    View buttonView;
    TextView toolbarTitle;
    Toolbar toolbar;
    SharedPreferences sharedPref;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrackerApplication application = (TrackerApplication) getActivity().getApplication();
        dao = application.getDatabase();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessions = application.getSessions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.exercises_list_fragment, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.exercise_toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setupTemplates(rootView);

        this.buttonView = inflater.inflate(R.layout.exercise_list_add_btn, container, false);

        Intent sessionIntent = getActivity().getIntent();

        //Get session information from main activity
        if (sessionIntent != null && sessionIntent.hasExtra(SessionsFragment.INTENT_KEY)) {

            currentSession = (Session) sessionIntent.getSerializableExtra
                    (SessionsFragment.INTENT_KEY);
            position = sessionIntent.getIntExtra(SessionsFragment.POSITION_KEY, 0);
            setExistingWorkout(currentSession);
        } else if (sessionIntent != null && sessionIntent.hasExtra(MainActivity.ADD_TASK)) {
            currentSession = (Session) sessionIntent.getSerializableExtra
                    (MainActivity.ADD_TASK);
            position = 0;
            getActivity().setTitle("Today's Workout");
        }
        sessionID = currentSession.getSessionId();

        adapter = new ExerciseAdapter((ExerciseActivity) getActivity(), sessions.get(position).workouts, dao);
        exerciseListView = (ListView) rootView.findViewById(R.id.exercises_list_main);
        exerciseListView.setAdapter(adapter);
        exerciseListView.addFooterView(buttonView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (fabExercise != null && adapter.getCount() > 0) {
            fabExercise.setVisibility(View.VISIBLE);
        }
    }


    public void setExistingWorkout(Session session) {
        String title;
        SimpleDateFormat fmt = new SimpleDateFormat("MMM dd");
        GregorianCalendar calendar = new GregorianCalendar();
        fmt.setCalendar(calendar);
        String todayDate = fmt.format(calendar.getTime());

        if (session.getDate().equals(todayDate)) {
            title = "Today's Workout";
        } else {
            title = session.getDate() + "   Session  #" + session.getSessionId();
        }
        if (toolbarTitle != null) toolbarTitle.setText(title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.exerciseMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setupTemplates(View rootView) {
        template_A = (ImageButton) rootView.findViewById(R.id.template_a);
        template_B = (ImageButton) rootView.findViewById(R.id.template_b);


        template_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String templateName = getString(R.string.template_A);
                boolean isTemplateEmpty = sharedPref.getBoolean(templateName,true);
                TemplateDialog dialog = TemplateDialog.newInstance(isTemplateEmpty,templateName);
                dialog.setTargetFragment(ExercisesFragment.this,TemplateDialog.DIALOG_REQUEST_CODE);
                dialog.show(getFragmentManager(), "TemplateDialog");
            }
        });
        template_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String templateName = getString(R.string.template_B);
                boolean isTemplateEmpty = sharedPref.getBoolean(templateName,true);
                TemplateDialog dialog = TemplateDialog.newInstance(isTemplateEmpty,templateName);
                dialog.setTargetFragment(ExercisesFragment.this,TemplateDialog.DIALOG_REQUEST_CODE);
                dialog.show(getFragmentManager(), "TemplateDialog");
            }
        });
    }


    /****************************
     * ASYNC LOADER FOR ADAPTER *
     ****************************/
    //Loads all workouts for current session in workouts list.
    @Override
    public Loader<List<Workout>> onCreateLoader(int id, Bundle args) {
        return new ExerciseLoader(getActivity().getApplicationContext(), dao, sessionID);
    }

    @Override
    public void onLoadFinished(Loader<List<Workout>> loader, List<Workout> data) {
        sessions.get(position).workouts.clear();
        sessions.get(position).workouts.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Workout>> loader) {
        sessions.get(position).workouts.clear();

    }

    /************************************************************/

    /*********************** Templat Handling ******************************************/

    //Receives data back from the dialog fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences.Editor editor = sharedPref.edit();
        if (requestCode == TemplateDialog.DIALOG_REQUEST_CODE) {
            String templateName = (data.hasExtra(TemplateDialog.TEMPLATE_NAME)) ?
                    data.getStringExtra(TemplateDialog.TEMPLATE_NAME) : "";

            if(data.hasExtra(TemplateDialog.SAVE_TEMPLATE)){
                if(sessions.get(position).workouts.size() > 0 ) {
                    //template is no longer empty
                    editor.putBoolean(templateName, false);
                    editor.apply();
                    new TemplateTask().execute(templateName,TemplateDialog.SAVE_TEMPLATE);
                }
            }
            else if(data.hasExtra(TemplateDialog.LOAD_TEMPLATE)){
                new TemplateTask().execute(templateName,TemplateDialog.LOAD_TEMPLATE);
            }
            else if(data.hasExtra(TemplateDialog.CLEAR_TEMPLATE)){
                editor.putBoolean(templateName,true);
                editor.apply();
                new TemplateTask().execute(templateName,TemplateDialog.CLEAR_TEMPLATE);
            }
        }
    }

    public class TemplateTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            String templateName = params[0];
            String action = params[1];
            switch (action) {
                case TemplateDialog.SAVE_TEMPLATE:
                    Log.v(TAG, "saving template");
                    dao.db.beginTransaction();
                    dao.deleteTemplate(templateName);
                    for (Workout workout : sessions.get(position).workouts) {
                        dao.saveWorkoutToTemplate(templateName, workout);
                    }
                    dao.db.setTransactionSuccessful();
                    dao.db.endTransaction();
                    break;
                case TemplateDialog.LOAD_TEMPLATE :
                    Log.v(TAG, "Loading template");
                    dao.db.beginTransaction();
                    if(sessions.get(position).workouts.size() > 0)
                        dao.deleteAllWorkouts(sessionID);
                    ArrayList<Workout> templateWorkouts = dao.loadTemplate(templateName, sessionID);
                    dao.db.setTransactionSuccessful();
                    dao.db.endTransaction();
                    if (templateWorkouts != null) {
                        getActivity().getSupportLoaderManager().restartLoader(R.id.exercise_loader_id, null,
                                ExercisesFragment.this);
                    }
                    break;
                case TemplateDialog.CLEAR_TEMPLATE :
                    Log.v(TAG, "Clearing template");
                    dao.deleteTemplate(templateName);
                    break;
            }
            return null;
        }
    }

}
