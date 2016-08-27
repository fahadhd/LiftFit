package com.example.fahadhd.bodybuildingtracker.exercises;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


public class ExercisesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Workout>>{
    TrackerDAO dao;
    ExerciseAdapter adapter;
    Session currentSession;
    long sessionID;
    int position;
    ArrayList<Session> sessions;
    ListView exerciseListView;
    Menu exerciseMenu;
    FloatingActionButton fabExercise;
    View buttonView;
    TextView toolbarTitle;
    Toolbar toolbar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrackerApplication application  = (TrackerApplication)getActivity().getApplication();
        dao = application.getDatabase();
        sessions = application.getSessions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.exercises_list_fragment, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.exercise_toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        this.buttonView =  inflater.inflate(R.layout.exercise_list_add_btn, container, false);
        adapter = new ExerciseAdapter((ExerciseActivity) getActivity(),sessions.get(position).workouts,dao);

        Intent sessionIntent = getActivity().getIntent();

        //Get session information from main activity
        if(sessionIntent != null && sessionIntent.hasExtra(SessionsFragment.INTENT_KEY)) {

            currentSession = (Session) sessionIntent.getSerializableExtra
                    (SessionsFragment.INTENT_KEY);
            position = sessionIntent.getIntExtra(SessionsFragment.POSITION_KEY,0);
            setExistingWorkout(currentSession);
        }
        else if (sessionIntent != null && sessionIntent.hasExtra(MainActivity.ADD_TASK)){
            currentSession = (Session) sessionIntent.getSerializableExtra
                    (MainActivity.ADD_TASK);
            position = 0;
            getActivity().setTitle("Today's Workout");
        }
        sessionID = currentSession.getSessionId();

        exerciseListView = (ListView)rootView.findViewById(R.id.exercises_list_main);
        exerciseListView.setAdapter(adapter);
        exerciseListView.addFooterView(buttonView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(fabExercise != null && adapter.getCount() > 0) {
            fabExercise.setVisibility(View.VISIBLE);
        }
    }


    public void setExistingWorkout(Session session){
        String title;
        SimpleDateFormat fmt = new SimpleDateFormat("MMM dd");
        GregorianCalendar calendar = new GregorianCalendar();
        fmt.setCalendar(calendar);
        String todayDate = fmt.format(calendar.getTime());

        if(session.getDate().equals(todayDate)){
            title = "Today's Workout";
        }
        else {
            title = session.getDate() + "   Session  #" + session.getSessionId();
        }
        if(toolbarTitle != null) toolbarTitle.setText(title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.exerciseMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }



    /*************** ASYNC LOADER FOR ADAPTER********************/
    //Loads all workouts for current session in workouts list.
    @Override
    public Loader<List<Workout>> onCreateLoader(int id, Bundle args) {
        return new ExerciseLoader(getActivity().getApplicationContext(),dao,sessionID);
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

    public void scrollMyListViewToBottom() {
        exerciseListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                exerciseListView.smoothScrollToPosition(adapter.getCount() - 1);
            }
        });
    }
}
