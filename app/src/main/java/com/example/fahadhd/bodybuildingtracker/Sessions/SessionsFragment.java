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
import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
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
public class SessionsFragment extends Fragment{

    public SessionsFragment() {
    }

    private SessionAdapter adapter;
    private ArrayList<Session> sessions;
    ListView sessionsListView;
    TrackerDAO dao;


    public static final String INTENT_KEY = "Session_ID";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrackerApplication application  = (TrackerApplication)getActivity().getApplication();
        dao = application.getDatabase();
        sessions = application.getSessions();
        if(sessions.isEmpty() || sessions.size() == 1){
          refreshData();
        }
        Intent sessionIntent = getActivity().getIntent();
        if(sessionIntent != null && sessionIntent.hasExtra(ExerciseActivity.SESSION_ID)) {
            new UpdateSession().execute(sessionIntent.getLongExtra(ExerciseActivity.SESSION_ID,0));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.sessions_list_fragment, container, false);
        adapter = new SessionAdapter(getActivity(),sessions);

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

    public void refreshSessionData(long sessionID){
        Session updatedSession = dao.getSession(sessionID);
        for(int i = 0 ; i < sessions.size(); i++){
            if(sessions.get(i).getSessionId() == sessionID){
                sessions.set(i,updatedSession);
                return;
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void refreshData(){
        new GetSessions().execute();
    }

    public class GetSessions extends AsyncTask<Void,Void,ArrayList<Session>> {

        @Override
        protected ArrayList<Session> doInBackground(Void... params) {
            return dao.getSessions();
        }

        @Override
        protected void onPostExecute(ArrayList<Session> result) {
            if(sessions.isEmpty()){
                sessions.addAll(result);
            }
            else{
                sessions.clear();
                sessions.addAll(result);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public class UpdateSession extends AsyncTask<Long,Void,Void>{

        @Override
        protected Void doInBackground(Long... params) {
            refreshSessionData(params[0]);
            return null;
        }
    }
}