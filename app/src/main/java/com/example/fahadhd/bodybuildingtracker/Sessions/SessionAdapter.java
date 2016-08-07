package com.example.fahadhd.bodybuildingtracker.Sessions;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
        TextView one_title, two_title, three_title;
        TextView one_row_one, one_row_two, two_row_one, two_row_two,three_row_one, three_row_two;
        TextView one_weight, two_weight, three_weight;
        /***************************************/

        public ViewHolder(View view) {
            this.date = (TextView) view.findViewById(R.id.date);
            this.user_weight = (TextView) view.findViewById(R.id.user_weight);
            this.sessionID = (TextView) view.findViewById(R.id.sessionID);

            this.one_title = (TextView) view.findViewById(R.id.workout_one_title);
            this.two_title = (TextView) view.findViewById(R.id.workout_two_title);
            this.three_title = (TextView) view.findViewById(R.id.workout_three_title);

            this.one_row_one = (TextView) view.findViewById(R.id.workout_one_row_one);
            this.one_row_two = (TextView) view.findViewById(R.id.workout_one_row_two);
            this.two_row_one = (TextView) view.findViewById(R.id.workout_two_row_one);
            this.two_row_two = (TextView) view.findViewById(R.id.workout_two_row_two);
            this.three_row_one = (TextView) view.findViewById(R.id.workout_three_row_one);
            this.three_row_two = (TextView) view.findViewById(R.id.workout_three_row_two);

            this.one_weight = (TextView) view.findViewById(R.id.workout_one_weight);
            this.two_weight = (TextView) view.findViewById(R.id.workout_two_weight);
            this.three_weight = (TextView) view.findViewById(R.id.workout_three_weight);
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

        Session session = sessions.get(position);
        viewHolder.date.setText(session.getDate());
        viewHolder.user_weight.setText(session.getWeight().toString());
        viewHolder.sessionID.setText(session.getSessionId().toString());
       setTextPreviews(session.getWorkouts(),viewHolder);
        return row;

    }


    public static void setTextPreviews(ArrayList<Workout> workouts, ViewHolder viewHolder){
        if(workouts == null) return;
        Workout workout_one = (workouts.size() > 0) ? workouts.get(0) : null;
        Workout workout_two = (workouts.size() > 1) ? workouts.get(1) : null;
        Workout workout_three = (workouts.size() > 2) ? workouts.get(2) : null;

        if(workout_one != null){
          String[] rows = Utility.previewTextHelper(workout_one);
            viewHolder.one_title.setText(workout_one.getName());
            viewHolder.one_row_one.setText(rows[0]);
            viewHolder.one_row_two.setText(rows[1]);
         viewHolder.one_weight.setText(Integer.toString(workout_one.getWeight()));
        }
        if(workout_two != null){
            String[] rows = Utility.previewTextHelper(workout_two);
            viewHolder.two_title.setText(workout_two.getName());
            viewHolder.two_row_one.setText(rows[0]);
            viewHolder.two_row_two.setText(rows[1]);
           viewHolder.two_weight.setText(Integer.toString(workout_two.getWeight()));
        }
        if(workout_three != null){
            String[] rows = Utility.previewTextHelper(workout_three);
            viewHolder.three_title.setText(workout_three.getName());
            viewHolder.three_row_one.setText(rows[0]);
            viewHolder.three_row_two.setText(rows[1]);
           viewHolder.three_weight.setText(Integer.toString(workout_three.getWeight()));
        }




    }

}
