package com.example.fahadhd.bodybuildingtracker.exercises;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.sessions.SessionsFragment;
import com.example.fahadhd.bodybuildingtracker.sessions.Session;
import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;
import com.example.fahadhd.bodybuildingtracker.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


public class ExercisesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Workout>> {
    public static final String TAG = ExercisesFragment.class.getSimpleName();
    public static final int DIALOG_REQUEST_CODE = 23;
    TrackerDAO dao;
    Typeface tekton;
    ExerciseAdapter adapter;
    Session currentSession;
    long sessionID;
    int position;
    boolean templateAEmpty, templateBEmpty;
    ArrayList<Session> sessions;
    ListView exerciseListView;
    Menu exerciseMenu;
    FloatingActionButton fabExercise;
    View buttonView;
    TextView toolbarTitle,template_A,template_B;
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
        tekton = Typeface.createFromAsset(getActivity().getAssets(),"TektonPro-Bold.otf");

        toolbar = (Toolbar) rootView.findViewById(R.id.exercise_toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(tekton);

        buttonView = inflater.inflate(R.layout.exercise_list_add_btn, container, false);
        templateAEmpty = sharedPref.getBoolean(getString(R.string.template_A),true);
        templateBEmpty = sharedPref.getBoolean(getString(R.string.template_B),true);

        boolean newExercise = false;
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
            newExercise = true;
            position = 0;
            if (toolbarTitle != null) toolbarTitle.setText("Today's Workout");
        }
        sessionID = currentSession.getSessionId();
        setupTemplates(rootView);

        adapter = new ExerciseAdapter((ExerciseActivity) getActivity(), sessions.get(position).workouts, dao,tekton);
        exerciseListView = (ListView) rootView.findViewById(R.id.exercises_list_main);
        exerciseListView.setAdapter(adapter);

        if (newExercise){
            if(templateAEmpty && templateBEmpty){
                WorkoutDialog dialog = new WorkoutDialog();
                dialog.show(getActivity().getFragmentManager(), "WorkoutDialog");
            }
            else {
                StartUpExerciseDialog dialog = StartUpExerciseDialog.newInstance
                        (new boolean[]{templateAEmpty,templateBEmpty});
                dialog.setTargetFragment(this, DIALOG_REQUEST_CODE);
                dialog.show(getFragmentManager(), "TemplateDialog");
            }
        }
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


    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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

        Log.v(TAG,"Template is "+sessions.get(position).getTemplateName());

        template_A = (TextView) rootView.findViewById(R.id.template_a);
        template_B = (TextView) rootView.findViewById(R.id.template_b);

        setTemplateActive();

        template_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String templateName = getString(R.string.template_A);
                templateAEmpty = sharedPref.getBoolean(templateName,true);
                TemplateDialog dialog = TemplateDialog.newInstance(templateAEmpty,templateName);
                dialog.setTargetFragment(ExercisesFragment.this,DIALOG_REQUEST_CODE);
                dialog.show(getFragmentManager(), "TemplateDialog");
            }
        });
        template_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String templateName = getString(R.string.template_B);
                templateBEmpty = sharedPref.getBoolean(templateName,true);
                TemplateDialog dialog = TemplateDialog.newInstance(templateBEmpty,templateName);
                dialog.setTargetFragment(ExercisesFragment.this,DIALOG_REQUEST_CODE);
                dialog.show(getFragmentManager(), "TemplateDialog");
            }
        });
    }



    public void startWorkoutTask(Workout workout){
        new WorkoutTask().execute(workout);
    }
    public void startWorkoutTask(Workout oldWorkout, Workout newWorkout){
        new WorkoutTask().execute(oldWorkout,newWorkout);
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

    //TODO: Instead of looping most of the time the workoutid is the same as its index in the array
    //so check that first to speed things up.
    public class WorkoutTask extends AsyncTask<Workout,Void,Void>{
        @Override
        protected Void doInBackground(Workout... params) {
            Workout workout = params[0];
            ArrayList<Workout> workouts = sessions.get(position).getWorkouts();
            switch (workout.getTask()){

                case Constants.WORKOUT_TASK.ADD_WORKOUT:
                    Workout newWorkout = dao.addWorkout(sessionID, workout.getName(), workout.getWeight(), workout.getMaxSets(), workout.getMaxReps());
                    workout.sets = dao.addSets(newWorkout.getWorkoutID(), newWorkout.getMaxSets());
                    getActivity().getSupportLoaderManager().restartLoader(R.id.exercise_loader_id, null, ExercisesFragment.this);
                    dao.updateTemplateToSession("None",sessionID);
                    break;

                case Constants.WORKOUT_TASK.UPDATE_WORKOUT:
                    Workout oldWorkout = params[0];
                    Workout updatedWorkout = params[1];
                    dao.db.beginTransaction();
                    dao.updateWorkout(oldWorkout,updatedWorkout);
                    updatedWorkout.sets = dao.addSets(updatedWorkout.getWorkoutID(),updatedWorkout.getMaxSets());
                    dao.db.setTransactionSuccessful();
                    dao.db.endTransaction();

                    //Updating cached data and restarting loader
                    getActivity().getSupportLoaderManager().restartLoader(R.id.exercise_loader_id, null, ExercisesFragment.this);
                    for(int i = 0; i< workouts.size(); i++){
                        if(workouts.get(i).getWorkoutID() == updatedWorkout.getWorkoutID()){
                            workouts.set(i,updatedWorkout);
                        }
                    }
                    break;

                case Constants.WORKOUT_TASK.DELETE_WORKOUT:
                    dao.deleteWorkout(params[0].getWorkoutID());
                    //Updating cached data and restarting loader
                    getActivity().getSupportLoaderManager().restartLoader(R.id.exercise_loader_id, null, ExercisesFragment.this);
                    for(int i = 0; i < workouts.size(); i++){
                        if(workouts.get(i).getWorkoutID() == params[0].getWorkoutID()){
                            workouts.remove(i);
                        }
                    }
                    dao.updateTemplateToSession("None",sessionID);
                    break;
            }
            return null;
        }
    }

    /*********************** Template Handling ******************************************/

    //Receives data back from the dialog fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences.Editor editor = sharedPref.edit();
        if (requestCode == DIALOG_REQUEST_CODE) {
            String templateName = (data.hasExtra(Constants.TEMPLATE_TASK.TEMPLATE_NAME)) ?
                    data.getStringExtra(Constants.TEMPLATE_TASK.TEMPLATE_NAME) : "";

            if(data.hasExtra(Constants.TEMPLATE_TASK.SAVE_TEMPLATE)){
                if(sessions.get(position).workouts.size() > 0 ) {
                    //template is no longer empty
                    editor.putBoolean(templateName, false);
                    editor.apply();
                    sessions.get(position).updateTemplateName(templateName);
                    setTemplateActive();
                    new TemplateTask().execute(templateName,Constants.TEMPLATE_TASK.SAVE_TEMPLATE);
                }
            }
            else if(data.hasExtra(Constants.TEMPLATE_TASK.LOAD_TEMPLATE)){
                sessions.get(position).updateTemplateName(templateName);
                setTemplateActive();
                new TemplateTask().execute(templateName,Constants.TEMPLATE_TASK.LOAD_TEMPLATE);
            }
            else if(data.hasExtra(Constants.TEMPLATE_TASK.CLEAR_TEMPLATE)){
                editor.putBoolean(templateName,true);
                editor.apply();
                if(sessions.get(position).getTemplateName().equals(templateName)) deactivateTemplates();
                new TemplateTask().execute(templateName,Constants.TEMPLATE_TASK.CLEAR_TEMPLATE);
            }
            else if(data.hasExtra(Constants.GENERAL.NEW_WORKOUT_DIALOG)){
                WorkoutDialog dialog = new WorkoutDialog();
                dialog.show(getActivity().getFragmentManager(), "WorkoutDialog");
            }
        }
    }

    public class TemplateTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            String templateName = params[0];
            String action = params[1];
            switch (action) {
                case Constants.TEMPLATE_TASK.SAVE_TEMPLATE:
                    Log.v(TAG, "saving template");
                    dao.db.beginTransaction();
                    dao.deleteTemplate(templateName);
                    dao.updateTemplateToSession(templateName,sessionID);

                    for (Workout workout : sessions.get(position).workouts)
                        dao.saveWorkoutToTemplate(templateName, workout);

                    dao.db.setTransactionSuccessful();
                    dao.db.endTransaction();
                    break;

                case Constants.TEMPLATE_TASK.LOAD_TEMPLATE :
                    Log.v(TAG, "Loading template");

                    dao.db.beginTransaction();
                    dao.updateTemplateToSession(templateName,sessionID);

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

                case Constants.TEMPLATE_TASK.CLEAR_TEMPLATE :
                    Log.v(TAG, "Clearing template");
                    if(sessions.get(position).getTemplateName().equals(templateName)) {
                        dao.updateTemplateToSession("None", sessionID);
                    }
                    dao.deleteTemplate(templateName);
                    break;
            }

            return null;
        }
    }

    public void setTemplateActive() {
        if (sessions.get(position).getTemplateName().equals(getString(R.string.template_A))){
            template_A.setBackgroundResource(R.drawable.template_active_selector);
            template_B.setBackgroundResource(R.drawable.template_inactive_selector);
        }
        if(sessions.get(position).getTemplateName().equals(getString(R.string.template_B))) {
            template_B.setBackgroundResource(R.drawable.template_active_selector);
            template_A.setBackgroundResource(R.drawable.template_inactive_selector);
        }
    }

    public void deactivateTemplates(){
        sessions.get(position).updateTemplateName("None");
        template_A.setBackgroundResource(R.drawable.template_inactive_selector);
        template_B.setBackgroundResource(R.drawable.template_inactive_selector);
    }

}
