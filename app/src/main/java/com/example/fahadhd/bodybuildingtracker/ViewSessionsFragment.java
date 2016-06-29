package com.example.fahadhd.bodybuildingtracker;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDbHelper;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * First view a user see's when starting the app. Displays all of the user's
 * past workout and allows the ability to add a new workout which will then start another
 * activity.
 */
public class ViewSessionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<String>>{

    public ViewSessionsFragment() {
    }

    static ArrayAdapter<String> adapter;
    static ArrayList<String> sessions = new ArrayList<String>();
    static ListView workouts;

    public static final String ON_CREATE_TASK = "ON_CREATE";
    public static final String SESSION_TASK = "Session";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(R.id.string_loader_id,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       //getContext().deleteDatabase(TrackerDbHelper.DATABASE_NAME);
        View rootView = inflater.inflate(R.layout.workouts_list_fragment, container, false);
        adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.workouts_list_item,
                R.id.list_item_workout,
                new ArrayList<String>());

        workouts = (ListView) rootView.findViewById(R.id.workout_list_main);


        workouts.setAdapter(adapter);



        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("adapter", sessions);
    }

    public void startSessionTask(){

        new addTasks().execute(SESSION_TASK);


    }
////////////////////////// ASYNC LOADER FOR LIST ADAPTERS/////////////////////////
    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new SessionLoader(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        adapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        adapter.clear();
    }

////////////////////////////// ASYNC TASK FOR BUTTONS /////////////////////////////////////
    public class addTasks extends AsyncTask<String,Void,ArrayList<String>> {
        String buttonType;

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            buttonType = params[0];
            if (buttonType.equals(SESSION_TASK)) {
                addSession();
                return new TrackerDAO(getContext()).getSessions();
            } else if (buttonType.equals(ON_CREATE_TASK)) {
                return new TrackerDAO(getContext()).getSessions();
            }
            else{
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            adapter.clear();
            adapter.addAll(result);
            workouts.setAdapter(adapter);
        }

        public long addSession() {
            SimpleDateFormat fmt = new SimpleDateFormat("MMM dd");
            GregorianCalendar calendar = new GregorianCalendar();
            fmt.setCalendar(calendar);
            String dateFormatted = fmt.format(calendar.getTime());
            //TODO: Add user_weight to settings and aquire it from preferences.
            int user_weight = 185;

            TrackerDAO dao = new TrackerDAO(getContext());
            return dao.addSession(dateFormatted, user_weight);

        }
    }

}