package com.example.fahadhd.bodybuildingtracker.Sessions;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fahadhd.bodybuildingtracker.Exercises.ExerciseActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * First view a user see's when starting the app. Displays all of the user's
 * past workout and allows the ability to add a new workout which will then start another
 * activity.
 */
public class ViewSessionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Session>>{

    public ViewSessionsFragment() {
    }

    static ArrayAdapter<String> adapter;
    static ArrayList<String> sessions_list = new ArrayList<String>();
    static ListView sessions;

    public static final String ON_CREATE_TASK = "ON_CREATE";
    public static final String SESSION_TASK = "Session";
    public static final String INTENT_KEY = "Session_ID";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Load all of the prior sessions when app starts
        getActivity().getSupportLoaderManager().initLoader(R.id.string_loader_id,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       //getContext().deleteDatabase(TrackerDbHelper.DATABASE_NAME);
        View rootView = inflater.inflate(R.layout.sessions_list_fragment, container, false);
        adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.sessions_list_item,
                R.id.list_item_session,
                sessions_list);

        sessions = (ListView) rootView.findViewById(R.id.session_list_main);


        sessions.setAdapter(adapter);
        sessions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(),ExerciseActivity.class);
                        //TODO: CHANGE ADAPTER TO ALLOW SESSION OBJECTS TO BE PASSED
                        //putExtra(INTENT_KEY,adapter.getItem(position));
                startActivity(intent);
            }
        });


        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("adapter", sessions_list);
    }

    public void startSessionTask(){
        new addTasks().execute(SESSION_TASK);
    }
////////////////////////// ASYNC LOADER FOR LIST ADAPTERS/////////////////////////
    /*Let the loader handle adding data to the list view of session*/
    @Override
    public Loader<List<Session>> onCreateLoader(int id, Bundle args) {
        return new SessionLoader(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Session>> loader, List<Session> data) {
        adapter.clear();
       for(Session session:data){
           adapter.add(session.toString());
       }
    }

    @Override
    public void onLoaderReset(Loader<List<Session>> loader) {
        adapter.clear();
    }




////////////////////////////// ASYNC TASK FOR BUTTONS /////////////////////////////////////
    /*Let async task handle actions dealing with the database and button actions.*/
    public class addTasks extends AsyncTask<String,Void,Void> {
        String buttonType;

        @Override
        protected Void doInBackground(String... params) {
            buttonType = params[0];
            if (buttonType.equals(SESSION_TASK)) {
                addSession();
                return null;
            }
            else{
                return null;
            }
        }

        public long addSession() {
            SimpleDateFormat fmt = new SimpleDateFormat("MMM dd");
            GregorianCalendar calendar = new GregorianCalendar();
            fmt.setCalendar(calendar);
            String dateFormatted = fmt.format(calendar.getTime());
            //TODO: Add user_weight to settings and acquire it from preferences.
            int user_weight = 185;

            TrackerDAO dao = new TrackerDAO(getContext());
            return dao.addSession(dateFormatted, user_weight);

        }
    }

}