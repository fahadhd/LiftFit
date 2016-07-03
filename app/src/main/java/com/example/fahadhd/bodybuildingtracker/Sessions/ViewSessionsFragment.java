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
import com.example.fahadhd.bodybuildingtracker.data.TrackerDbHelper;

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

    private SessionAdapter adapter;
    private ArrayList<Session> sessions = new ArrayList<>();
    ListView sessionsListView;


    public static final String ADD_SESSION = "Add_Session";
    public static final String INTENT_KEY = "Session_ID";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().initLoader(R.id.string_loader_id,null,this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getContext().deleteDatabase(TrackerDbHelper.DATABASE_NAME);
        adapter = new SessionAdapter(getActivity(),sessions);
        View rootView = inflater.inflate(R.layout.sessions_list_fragment, container, false);


        sessionsListView = (ListView) rootView.findViewById(R.id.session_list_main);


        sessionsListView.setAdapter(adapter);
        sessionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(),ExerciseActivity.class).
                        putExtra(INTENT_KEY,sessions.get(position));
                startActivity(intent);
            }
        });


        return rootView;
    }


    public void startSessionTask(){
        new AddSessionTask().execute();
    }


////////////////////////// ASYNC LOADER FOR LIST ADAPTERS/////////////////////////
    /*Let the loader handle adding data to the list view of session*/
    @Override
    public Loader<List<Session>> onCreateLoader(int id, Bundle args) {
        return new SessionLoader(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Session>> loader, List<Session> data) {

       if(sessions.size() == 0){
           for(Session session:data){
               sessions.add(0,session);
           }
       }
        else{
           //TODO: Find way to cache sessions data, below doesn't get called ever.
           sessions.add(0,data.get(data.size()-1));
       }
    }

    @Override
    public void onLoaderReset(Loader<List<Session>> loader) {

    }

    /*Adds a new session to the database*/
    public class AddSessionTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            addSession();
            return null;
        }

        public void addSession() {
            SimpleDateFormat fmt = new SimpleDateFormat("MMMM dd");
            GregorianCalendar calendar = new GregorianCalendar();
            fmt.setCalendar(calendar);
            String dateFormatted = fmt.format(calendar.getTime());
            //TODO: Add user_weight to settings and acquire it from preferences.
            int user_weight = 185;

            TrackerDAO dao = new TrackerDAO(getContext());
            dao.addSession(dateFormatted, user_weight);
        }
    }

}