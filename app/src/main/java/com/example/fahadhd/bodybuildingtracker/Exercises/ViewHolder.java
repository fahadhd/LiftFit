package com.example.fahadhd.bodybuildingtracker.Exercises;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.R;

public class ViewHolder{
    TextView name,orderNum, sets, separator, reps, weight,completed_dialog;
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
        completed_dialog = (TextView) view.findViewById(R.id.workout_complete_dialog);

    }
}