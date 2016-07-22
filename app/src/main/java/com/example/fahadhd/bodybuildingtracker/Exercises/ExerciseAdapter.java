package com.example.fahadhd.bodybuildingtracker.Exercises;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.Utility;
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


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WorkoutViewHolder viewHolder;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.exercises_list_item, null);
            viewHolder = new WorkoutViewHolder(row);
            row.setTag(viewHolder);
        }
        else{
            viewHolder = (WorkoutViewHolder)row.getTag();
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
            addButtons(viewHolder, workout,viewHolder);
        }

        //TODO: App is a little slow when running this code, optimize later possibly put in thread.
        boolean sets_started = Utility.allSetsStarted(workout);
        boolean sets_finished = (sets_started && Utility.allSetsFinished(workout));
        if(sets_started && !sets_finished ){
            viewHolder.completed_dialog.setText("You'll get in next time!");
        }
       else if(sets_finished){
            viewHolder.completed_dialog.setText("Congrats +5 lb next time!");
        }



        return row;
    }

    public void addButtons(WorkoutViewHolder views, Workout workout, WorkoutViewHolder viewHolder){
        int max_sets = workout.getMaxSets();
        int i = 0;
        ArrayList<Set> setList = workout.getSets();
        Set currSet;
        Button setButton;

        while (i < 3 && i < max_sets) {
            setButton = new Button(context);
            currSet = setList.get(i);

            setButton.setOnClickListener(new
                    SetListener(setButton,dao,workout,currSet,viewHolder,context));

            views.setOne.addView(setButton);

            i++;
        }

        while (i < 6 && i < max_sets) {
            setButton = new Button(context);
            currSet = setList.get(i);

            setButton.setOnClickListener(new
                    SetListener(setButton, dao, workout, currSet,viewHolder,context));

            views.setTwo.addView(setButton);
            i++;
        }

    }


}
