package com.example.fahadhd.bodybuildingtracker.sessions;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;
import com.example.fahadhd.bodybuildingtracker.exercises.Workout;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.utilities.Utility;

import java.util.ArrayList;


public class SessionAdapter extends BaseSwipeAdapter {
    MainActivity activity;
    ArrayList<Session> sessions;
    TrackerDAO dao;
    Typeface tekton;

    private static final String TAG = SessionAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_COUNT = 2;
    private static final int TOP_SESSION_VIEW = 0;
    private static final int  PAST_SESSION_VIEW = 1;
    int recentPosition = -1;

    public SessionAdapter(MainActivity mActivity, ArrayList<Session> sessions) {
        this.activity = mActivity;

        tekton = Typeface.createFromAsset(mActivity.getAssets(),"TektonPro-Bold.otf");

        TrackerApplication application = (TrackerApplication) activity.getApplication();
        dao = application.getDatabase();
        this.sessions = application.getSessions();
    }

    public void setRecentPosition(int recentPosition){
        this.recentPosition = recentPosition;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? TOP_SESSION_VIEW : PAST_SESSION_VIEW;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getCount() {
        return sessions.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public class ViewHolder {
        TextView date, user_weight,sessionID,templateName;
        ImageView listBar;
        ImageButton deleteSession;

        /********* Workout Previews ************/
        TextView previewOne, previewTwo,previewThree;

        /***************************************/

        public ViewHolder(View view) {
            this.date = (TextView) view.findViewById(R.id.date);
            this.user_weight = (TextView) view.findViewById(R.id.user_weight);
            this.sessionID = (TextView) view.findViewById(R.id.session_num);
            this.templateName = (TextView) view.findViewById(R.id.session_template);

            this.previewOne = (TextView) view.findViewById(R.id.preview_one);
            this.previewTwo = (TextView) view.findViewById(R.id.preview_two);
            this.previewThree = (TextView) view.findViewById(R.id.preview_three);

            this.listBar = (ImageView) view.findViewById(R.id.list_bar);
            this.deleteSession = (ImageButton) view.findViewById(R.id.delete_confirm);
        }

    }



    @Override
    public int getSwipeLayoutResourceId(int position) {
        if(position == 0){
            return R.id.session_top_item;
        }
        else {
            return R.id.session_list_item;
        }
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        int type = getItemViewType(position);
        View row = null;
        LayoutInflater inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (type){
            case TOP_SESSION_VIEW :
                row = inflater.inflate(R.layout.session_top_item, parent, false);
                break;
            case PAST_SESSION_VIEW :
                row = inflater.inflate(R.layout.sessions_list_item, parent, false);
                ViewHolder viewHolder = new ViewHolder(row);
                row.setTag(viewHolder);
                break;
        }
        return row;
    }



    @Override
    public void fillValues(int position, View convertView) {
        int type = getItemViewType(position);
        Session session = sessions.get(position);

        if (type == TOP_SESSION_VIEW) {
            setUpTopSession(session,convertView);
        }
        else {
            setUpSession(position,session,convertView);
        }

    }


    public void setUpTopSession(final Session session, View view){
        Utility.setTopSessionPreviews(session.getWorkouts(), view, activity, tekton);
        Utility.setTopInfoText(view, session, tekton, activity);
        TextView deleteSession = (TextView) view.findViewById(R.id.top_delete_confirm);
        deleteSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessions.remove(0);
                //if(cachedSessions.size() > 0 ) cachedSessions.remove(0);
                closeItem(0);
                notifyDataSetChanged();
                //activity.recreate();
                new DeleteTask().execute(session.getSessionId());

            }
        });

    }

    public  void setUpSession(final int position, final Session session, View view){
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.listBar.setBackgroundResource(R.drawable.list_bar);
        int user_weight = Utility.getUpdatedValue(session.getWeight(), activity);

        setInfoText("Date", session.getDate(), viewHolder);
        setInfoText("Session", Long.toString(session.getSessionId()), viewHolder);
        setInfoText("Weight", Integer.toString(user_weight), viewHolder);
        String templateName = (session.getTemplateName().equals("None")) ? "N/A" : session.getTemplateName();
        setInfoText("Template", templateName, viewHolder);

        setPreviewText(session.getWorkouts(), viewHolder);
        //Colors the recently exited session with a gray background

        viewHolder.deleteSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessions.remove(position);
               // if(cachedSessions.size() > position) cachedSessions.remove(position);
                //activity.recreate();
                notifyDataSetChanged();
                closeItem(position);
                new DeleteTask().execute(session.getSessionId());
            }
        });

        if (recentPosition == position && position != 0) {
            viewHolder.listBar.setBackgroundResource(R.drawable.list_bar_recent);
        }
    }


    //Text for the polygons for each session
    public void setInfoText(String title, String description, ViewHolder viewHolder){
        switch (title) {
            case "Date": viewHolder.date.setText(Html.fromHtml("<small><font color=\"black\">" + title + "</font></small>" + "<br />" +
                    "<small><b>" + description + "</b></small>"));
            case "Session": viewHolder.sessionID.setText(Html.fromHtml("<small><font color=\"black\">" + title + "</font></small>" + "<br />" +
                    "<b>" + description + "</b>"));
            case "Weight": viewHolder.user_weight.setText(Html.fromHtml("<small><font color=\"black\">" + title + "</font></small>" + "<br />" +
                    "<b>" + description + "</b>"));
            case "Template": viewHolder.templateName.setText(Html.fromHtml("<small><font color=\"black\">" + title + "</font></small>" + "<br />" +
                    "<b>" + description + "</b>"));
        }

    }
    //Sets the previews of 0-3 workouts for each session below the top session view.
    public void setPreviewText(ArrayList<Workout> workouts, ViewHolder viewHolder){
        if(workouts == null) return;
        Workout workout_one = (workouts.size() > 0) ? workouts.get(0) : null;
        Workout workout_two = (workouts.size() > 1) ? workouts.get(1) : null;
        Workout workout_three = (workouts.size() > 2) ? workouts.get(2) : null;


        if(workout_one != null){
            CharSequence rows = Utility.previewTextHelper(workout_one, activity);
            viewHolder.previewOne.setText(rows);
            viewHolder.previewOne.setTypeface(tekton);
        }
        else{
            viewHolder.previewOne.setText("");
        }
        if(workout_two != null){
            CharSequence rows = Utility.previewTextHelper(workout_two, activity);
            viewHolder.previewTwo.setText(rows);
            viewHolder.previewTwo.setTypeface(tekton);

        }
        else{
            viewHolder.previewTwo.setText("");
        }
        if(workout_three != null){
            CharSequence rows = Utility.previewTextHelper(workout_three, activity);
            viewHolder.previewThree.setText(rows);
            viewHolder.previewThree.setTypeface(tekton);

        }
        else{
            viewHolder.previewThree.setText("");
        }
    }


    public class DeleteTask extends AsyncTask<Long,Void,Void>{

        @Override
        protected Void doInBackground(Long... params) {
            dao.deleteSession(params[0]);
            return null;
        }

    }



}
