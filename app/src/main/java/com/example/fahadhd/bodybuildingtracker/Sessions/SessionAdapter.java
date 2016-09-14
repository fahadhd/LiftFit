package com.example.fahadhd.bodybuildingtracker.sessions;

import android.content.Context;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.exercises.Set;
import com.example.fahadhd.bodybuildingtracker.exercises.Workout;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.utilities.Utility;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
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

        Session session = sessions.get(position);
        if(type == TOP_SESSION_VIEW) {
            Utility.setTopSessionPreviews(session.getWorkouts(),row,context,tekton);
            setTopInfoText(row,session);
            return row;
        }
        else {
            int user_weight = Utility.getUpdatedValue(session.getWeight(),context);
            viewHolder.listBar.setImageResource(R.drawable.list_bar);
            setInfoText("Date", session.getDate(), viewHolder);
            setInfoText("Session", Long.toString(session.getSessionId()), viewHolder);
            setInfoText("Weight", Integer.toString(user_weight), viewHolder);

            String templateName = (session.getTemplateName().equals("None")) ? "N/A" : session.getTemplateName();
            setInfoText("Template", templateName, viewHolder);

            setPreviewText(session.getWorkouts(), viewHolder);
            //Colors the recently exited session with a gray background

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

    public void setTopInfoText(View topView, Session session){
        TextView date, sessionNum, userWeight, sessionTemplate;
        int titleColor = ContextCompat.getColor(context,R.color.orange_a400);
        int start = 0, end;

        /******* Date View *******/
        date = (TextView) topView.findViewById(R.id.date);
        SpannableStringBuilder dateText = new SpannableStringBuilder("Date\n");
        end = dateText.length();
        dateText.setSpan(new ForegroundColorSpan(titleColor),start,end,0);
        dateText.append(session.getDate());
        start = end;
        end = dateText.length();
        dateText.setSpan(new RelativeSizeSpan(1.3f),start,end,0);
        date.setText(dateText);
        date.setTypeface(tekton);

        /******* Session Number View *******/
        sessionNum = (TextView) topView.findViewById(R.id.session_num);
        SpannableStringBuilder sesNumText = new SpannableStringBuilder("Session\n");
        start = 0; end = sesNumText.length();
        sesNumText.setSpan(new ForegroundColorSpan(titleColor),start,end,0);
        sesNumText.append(Long.toString(session.getSessionId()));
        start = end;
        end = sesNumText.length();
        sesNumText.setSpan(new RelativeSizeSpan(1.3f),start,end,0);
        sessionNum.setText(sesNumText);
        sessionNum.setTypeface(tekton);

        /******* User Weight View *******/
        userWeight = (TextView) topView.findViewById(R.id.user_weight);
        SpannableStringBuilder weightText = new SpannableStringBuilder("My Weight\n");
        start = 0; end = weightText.length();
        weightText.setSpan(new ForegroundColorSpan(titleColor),start,end,0);
        int weight = Utility.getUpdatedValue(session.getWeight(),context);
        weightText.append(Long.toString(weight));
        start = end;
        end = weightText.length();
        weightText.setSpan(new RelativeSizeSpan(1.3f),start,end,0);
        userWeight.setText(weightText);
        userWeight.setTypeface(tekton);

        /******* Session Template View *******/
        sessionTemplate = (TextView) topView.findViewById(R.id.session_template);
        SpannableStringBuilder sesTempText = new SpannableStringBuilder("Template\n");
        start = 0; end = sesTempText.length();
        sesTempText.setSpan(new ForegroundColorSpan(titleColor),start,end,0);
        sesTempText.append(session.getTemplateName());
        start = end;
        end = sesTempText.length();
        sesTempText.setSpan(new RelativeSizeSpan(1.3f),start,end,0);
        sessionTemplate.setText(sesTempText);
        sessionTemplate.setTypeface(tekton);
    }



}
