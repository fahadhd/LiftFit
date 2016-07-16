package com.example.fahadhd.bodybuildingtracker.Exercises;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.Sessions.SessionAdapter;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.util.ArrayList;

//Populates the list_view of the exercises fragment.
public class ExerciseAdapter extends BaseAdapter{
    Context context;
    ArrayList<Workout> workouts;
    TrackerDAO dao;

    public ExerciseAdapter(Context context, ArrayList<Workout> workouts, TrackerDAO dao){
        this.context = context;
        this.workouts = workouts;
        this.dao = dao;
    }

    @Override
    public int getCount() {
        return workouts.size();
    }

    @Override
    public Object getItem(int position) {
        return workouts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        TextView name,orderNum, sets, separator, reps, weight;
        //TODO: ADD third set underneath second.
        LinearLayout setOne,setTwo,setThree;
        public ViewHolder(View view){
            name = (TextView)view.findViewById(R.id.exercises_item_name);
            sets = (TextView)view.findViewById(R.id.exercises_item_sets);
            separator = (TextView)view.findViewById(R.id.exercises_item_separator);
            reps = (TextView)view.findViewById(R.id.exercises_item_reps);
            weight = (TextView)view.findViewById(R.id.exercises_item_weight);
            orderNum = (TextView)view.findViewById(R.id.exercises_item_order);
            setOne = (LinearLayout) view.findViewById(R.id.button_container_set_1);
            setTwo = (LinearLayout) view.findViewById(R.id.button_container_set_2);

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.exercises_list_item, null);
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)row.getTag();
        }
        Workout workout = workouts.get(position);

        viewHolder.name.setText(workout.getName());
        viewHolder.orderNum.setText(Integer.toString(workout.getOrderNum()));
        viewHolder.sets.setText(Integer.toString(workout.getMaxSets()));
        viewHolder.separator.setText("X");
        viewHolder.reps.setText(Integer.toString(workout.getMaxReps()));
        viewHolder.weight.setText(Integer.toString(workout.getWeight()));

        //Load buttons only once.
        if(viewHolder.setOne.getChildCount() == 0) {
            addButtons(viewHolder, workout);
        }

        return row;
    }

    public void addButtons(ViewHolder views, Workout workout){
        int max_sets = workout.getMaxSets();
        int i = 0;
        ArrayList<Set> setList = workout.getSets();
        Set currSet;
        Button setButton;

        while (i < 3 && i < max_sets) {
            setButton = new Button(context);
            currSet = setList.get(i);

            setButton.setOnClickListener(new
                    SetListener(setButton,dao,workout,currSet));

            views.setOne.addView(setButton);

            i++;
        }

        while (i < 6 && i < max_sets) {
            setButton = new Button(context);
            currSet = setList.get(i);

            setButton.setOnClickListener(new
                    SetListener(setButton, dao, workout, currSet));

            views.setTwo.addView(setButton);
            i++;
        }

    }


}
