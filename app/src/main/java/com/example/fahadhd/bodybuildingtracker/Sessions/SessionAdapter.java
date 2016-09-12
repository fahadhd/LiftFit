package com.example.fahadhd.bodybuildingtracker.sessions;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.exercises.Workout;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.utilities.Utility;

import java.util.ArrayList;


public class SessionAdapter extends BaseAdapter {
    Context context;
    ArrayList<Session> sessions;
    Typeface tekton;
    int recentPosition = -1;

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int TOP_SESSION_VIEW = 0;
    private static final int  PAST_SESSION_VIEW = 1;

    public SessionAdapter(Context mContext, ArrayList<Session> sessions) {
        this.context = mContext;
        this.sessions = sessions;
        tekton = Typeface.createFromAsset(mContext.getAssets(),"TektonPro-Bold.otf");
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

        }

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;
        int type = getItemViewType(position);

        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Initializing new row
        if (row == null) {
            switch (type){
                case TOP_SESSION_VIEW :
                    row = inflater.inflate(R.layout.session_top_item, parent, false);
                    break;
                case PAST_SESSION_VIEW :
                    row = inflater.inflate(R.layout.sessions_list_item, parent, false);
                    break;
            }
            viewHolder = new ViewHolder(row);
            if(row != null) row.setTag(viewHolder);
        }
        //Recycling views
        else {
            viewHolder = (ViewHolder) row.getTag();
        }
        if(type == TOP_SESSION_VIEW) {
            return row;
        }
        else {
            viewHolder.listBar.setImageResource(R.drawable.list_bar);
            /*****************Shared Preferences Info for user weight*****************************/
            Session session = sessions.get(position);
            int user_weight = session.getWeight();
            //Change from pounds to kilograms if that is what user prefers
            /**********************************************/

            setInfoText("Date", session.getDate(), viewHolder);
            setInfoText("Session", Long.toString(session.getSessionId()), viewHolder);
            setInfoText("Weight", Integer.toString(user_weight), viewHolder);

            String templateName = (session.getTemplateName().equals("None")) ? "N/A" : session.getTemplateName();
            setInfoText("Template", templateName, viewHolder);

            setPreviewText(session.getWorkouts(), viewHolder);
            if (recentPosition == position && position != 0) {
                viewHolder.listBar.setImageResource(R.drawable.list_bar_recent);
            }
            return row;
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
    //Sets the previews of 0-3 workouts for each session in the list view.
    public void setPreviewText(ArrayList<Workout> workouts, ViewHolder viewHolder){
        if(workouts == null) return;
        Workout workout_one = (workouts.size() > 0) ? workouts.get(0) : null;
        Workout workout_two = (workouts.size() > 1) ? workouts.get(1) : null;
        Workout workout_three = (workouts.size() > 2) ? workouts.get(2) : null;

        if(workout_one != null){
            CharSequence rows = Utility.previewTextHelper(workout_one,context);
            viewHolder.previewOne.setText(rows);
            viewHolder.previewOne.setTypeface(tekton);

        }
        else{
            viewHolder.previewOne.setText("");
        }
        if(workout_two != null){
            CharSequence rows = Utility.previewTextHelper(workout_two,context);
            viewHolder.previewTwo.setText(rows);
            viewHolder.previewTwo.setTypeface(tekton);

        }
        else{
            viewHolder.previewTwo.setText("");
        }
        if(workout_three != null){
            CharSequence rows = Utility.previewTextHelper(workout_three,context);
            viewHolder.previewThree.setText(rows);
            viewHolder.previewThree.setTypeface(tekton);

        }
        else{
            viewHolder.previewThree.setText("");
        }
    }
}
