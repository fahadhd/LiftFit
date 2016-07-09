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

        if(viewHolder.setOne.getChildCount() == 0) {
            addButtons(viewHolder, workout.getMaxSets(), workout.getMaxReps(), workout.getSets());
        }

        return row;
    }

    public void addButtons(ViewHolder views, int max_sets, int maxRep, ArrayList<Set> sets){
        int i = 0;
        long workoutKey;
        int orderNum, currRep;

        while (i < 3 && i < max_sets) {
            Button set = new Button(context);
            Set currSet = sets.get(i);
            workoutKey = currSet.getWorkoutID();
            orderNum = currSet.getSetNum();
            currRep = currSet.getCurrRep();
            if(currRep != -1) {
                set.setText(Integer.toString(currRep));
            }
            initButton(set, workoutKey, orderNum, currRep, maxRep);

            views.setOne.addView(set);

            i++;
        }

        while (i < 6 && i < max_sets) {
            Button set = new Button(context);
            set.setText(Integer.toString(sets.get(i).getCurrRep()));
            views.setTwo.addView(set);
            i++;
        }

    }

    //Sets an onClickListener to each button in the view using an anonymous inner class.
    public void initButton(Button set, long workoutKey, int orderNum, int currRep, int maxRep){
        set.setOnClickListener(new View.OnClickListener() {
            Button set;
            long workoutKey;
            int orderNum,currRep,maxRep;
            public View.OnClickListener init(Button set, long workoutKey, int orderNum,
                                             int currRep, int maxRep){
                this.set = set;
                this.workoutKey = workoutKey;
                this.orderNum = orderNum;
                this.currRep = currRep;
                this.maxRep = maxRep;
                return this;
            }
            @Override
            public void onClick(View v) {
                currRep = dao.updateRep(workoutKey,orderNum,currRep,maxRep);
                set.setText(Integer.toString(currRep));
            }

        }.init(set,workoutKey,orderNum,currRep,maxRep));
    }


}
