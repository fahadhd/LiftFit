package com.example.fahadhd.bodybuildingtracker.sessions;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.exercises.ExerciseActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * First view a user see's when starting the app. Displays all of the user's
 * past workout and allows the ability to add a new workout which will then start another
 * activity.
 */
public class SessionsFragment extends Fragment{

    private SessionAdapter adapter;
    private ArrayList<Session> sessions;
    ListView sessionsListView;
    TrackerDAO dao;
    Typeface tekton;
    int recentPosition = -1;

    public static final String POSITION_KEY = "position";
    public static final String INTENT_KEY = "Session_ID";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrackerApplication application  = (TrackerApplication)getActivity().getApplication();
        dao = application.getDatabase();
        sessions = application.getSessions();
        if(sessions.isEmpty() || sessions.size() == 1){
            new GetSessions().execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new SessionAdapter(getActivity(),sessions);
        tekton = Typeface.createFromAsset(getActivity().getAssets(),"TektonPro-Bold.otf");
//        if(sessions.isEmpty()){
//            View rootView =  inflater.inflate(R.layout.empty_sessions, container, false);
//            TextView userMsg = (TextView) rootView.findViewById(R.id.user_info);
//            userMsg.setTypeface(tekton);
//            return rootView;
//        }
        Intent exerciseIntent = getActivity().getIntent();
        if(exerciseIntent != null && exerciseIntent.hasExtra(POSITION_KEY)) {
            recentPosition = exerciseIntent.getIntExtra(POSITION_KEY, -1);
            adapter.setRecentPosition(recentPosition);
        }

        View rootView = inflater.inflate(R.layout.sessions_list_fragment, container, false);

        sessionsListView = (ListView) rootView.findViewById(R.id.session_list_main);


        sessionsListView.setAdapter(adapter);
        sessionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(),ExerciseActivity.class).
                        putExtra(INTENT_KEY,sessions.get(position)).putExtra(POSITION_KEY,position);
                startActivity(intent);
            }
        });



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        if(sessionsListView != null && recentPosition != -1) {

            sessionsListView.smoothScrollToPosition(recentPosition, 800);
        }
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

}