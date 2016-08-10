package com.example.fahadhd.bodybuildingtracker.Sessions;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.Exercises.Set;
import com.example.fahadhd.bodybuildingtracker.Exercises.Workout;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.Utility;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class SessionAdapter extends BaseAdapter {
    Context context;
    ArrayList<Session> sessions;

    public SessionAdapter(Context mContext, ArrayList<Session> sessions) {
        this.context = mContext;
        this.sessions = sessions;
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
        TextView date;
        TextView user_weight;
        TextView sessionID;

        /********* Workout Previews ************/
        TextView previewOne, previewTwo,previewThree;

        /***************************************/

        public ViewHolder(View view) {
            this.date = (TextView) view.findViewById(R.id.date);
            this.user_weight = (TextView) view.findViewById(R.id.user_weight);
            this.sessionID = (TextView) view.findViewById(R.id.sessionID);

            this.previewOne = (TextView) view.findViewById(R.id.preview_one);
            this.previewTwo = (TextView) view.findViewById(R.id.preview_two);
            this.previewThree = (TextView) view.findViewById(R.id.preview_three);

        }

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;

        //Initializing new row
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.sessions_list_item, parent, false);
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        }
        //Recycling views
        else {
            viewHolder = (ViewHolder) row.getTag();
        }
        /*****************Shared Preferences Info for user weight*****************************/
        SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(context);
        String pounds = context.getString(R.string.pref_units_pounds);
        String unit = shared_pref.getString(context.getString(R.string.pref_unit_list_key),pounds);
        Session session = sessions.get(position);
        int user_weight = session.getWeight();
        Double kiloWeight = user_weight*0.45359237;
        //Change from pounds to kilograms if that is what user prefers
        if(!unit.equals(pounds)) user_weight = kiloWeight.intValue();
        /**********************************************/

        setInfoText("Date",session.getDate(),viewHolder);
        setInfoText("Session",Long.toString(session.getSessionId()),viewHolder);
        setInfoText("Weight",Integer.toString(user_weight),viewHolder);

        setPreviewText(session.getWorkouts(),viewHolder);
        return row;

    }
    //Text for the polygons for each session
    public void setInfoText(String title, String description, ViewHolder viewHolder){
        switch (title) {
            case "Date": viewHolder.date.setText(Html.fromHtml("<small><font color=\"black\">" + title + "</font></small>" + "<br />" +
                    "<b>" + description + "</b>"));
            case "Session": viewHolder.sessionID.setText(Html.fromHtml("<small><font color=\"black\">" + title + "</font></small>" + "<br />" +
                    "<b>" + description + "</b>"));
            case "Weight": viewHolder.user_weight.setText(Html.fromHtml("<small><font color=\"black\">" + title + "</font></small>" + "<br />" +
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

        }
        else{
            viewHolder.previewOne.setText("");
        }
        if(workout_two != null){
            CharSequence rows = Utility.previewTextHelper(workout_two,context);
            viewHolder.previewTwo.setText(rows);

        }
        else{
            viewHolder.previewTwo.setText("");
        }
        if(workout_three != null){
            CharSequence rows = Utility.previewTextHelper(workout_three,context);
            viewHolder.previewThree.setText(rows);

        }
        else{
            viewHolder.previewThree.setText("");
        }
    }
}
