package com.example.fahadhd.bodybuildingtracker.Exercises;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.R;

import java.util.ArrayList;

public class ExerciseAdapter extends BaseAdapter{
    Context context;
    ArrayList<Workout> workouts;

    public ExerciseAdapter(Context context, ArrayList<Workout> workouts){
        this.context = context;
        this.workouts = workouts;
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
        public ViewHolder(View view){
            name = (TextView)view.findViewById(R.id.exercises_item_name);
            sets = (TextView)view.findViewById(R.id.exercises_item_sets);
            separator = (TextView)view.findViewById(R.id.exercises_item_separator);
            reps = (TextView)view.findViewById(R.id.exercises_item_reps);
            weight = (TextView)view.findViewById(R.id.exercises_item_weight);
            orderNum = (TextView)view.findViewById(R.id.exercises_item_order);
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

        return row;
    }
}
