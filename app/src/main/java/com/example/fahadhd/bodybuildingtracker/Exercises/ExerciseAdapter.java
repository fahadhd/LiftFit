package com.example.fahadhd.bodybuildingtracker.exercises;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.utilities.Utility;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import org.apache.commons.lang3.text.WordUtils;

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
    public boolean isEnabled(int position) {
        return false;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WorkoutViewHolder viewHolder;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.exercises_list_item, parent,false);
            viewHolder = new WorkoutViewHolder(row);
            row.setTag(viewHolder);
        }
        else{
            viewHolder = (WorkoutViewHolder)row.getTag();
        }
        Workout workout = workouts.get(position);

        viewHolder.workoutInfo.setText(spanWorkoutInfo(workout));
        activateButtons(viewHolder,workout);




        //TODO: App is a little slow when running this code, optimize later possibly put in thread.
//        boolean sets_started = Utility.allSetsStarted(workout);
//        boolean sets_finished = (sets_started && Utility.allSetsFinished(workout));
//        if(sets_started && !sets_finished ){
//            //viewHolder.completed_dialog.setText("You'll get in next time!");
//        }
//        else if(sets_finished){
//            //viewHolder.completed_dialog.setText("Congrats +5 lb next time!");
//        }

        return row;
    }
    public void activateButtons(WorkoutViewHolder viewHolder, Workout workout){
        int maxSets = workout.getMaxSets();
        ArrayList<Set> setList = workout.getSets();
        TextView currButton;
        Set currSet;
        for(int i = 0; i < maxSets; i++){
            currSet = setList.get(i);

            switch (i){
                case 0: currButton = viewHolder.buttonOne; break;
                case 1: currButton = viewHolder.buttonTwo; break;
                case 2: currButton = viewHolder.buttonThree; break;
                case 3: currButton = viewHolder.buttonFour; break;
                case 4: currButton = viewHolder.buttonFive; break;
                case 5: currButton = viewHolder.buttonSix; break;
                case 6: currButton = viewHolder.buttonSeven; break;
                case 7:currButton = viewHolder.buttonEight; break;
                default: currSet = setList.get(0); currButton = viewHolder.buttonOne;
            }
            currButton.setVisibility(View.VISIBLE);
            currButton.setOnClickListener(new
                    SetListener(currButton,dao,workout,currSet,viewHolder,context));
        }
    }


    //TODO: Put in utility class and use it for session list item as well.
    public SpannableStringBuilder spanWorkoutInfo(Workout workout){
        int start, end;
        String buffer = WordUtils.capitalizeFully(workout.getName().replaceAll("\\s+","\n"));
        end = buffer.length();
        /******************Workout Title********************/
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(buffer);
        //spanBuilder.setSpan(new ForegroundColorSpan(Color.BLACK),start,end,0);
        /******************Workout Weight********************/
        String unit = Utility.getUnit(context);
        Double weightUnit = (unit.equals("LB")) ? workout.getWeight(): workout.getWeight()*0.45359237;
        buffer = '\n'+" "+Integer.toString(weightUnit.intValue());
        spanBuilder.append(buffer);
        int weightColor = ContextCompat.getColor(context,R.color.orange_a400);
        start = end;
        end = spanBuilder.length();
        spanBuilder.setSpan(new ForegroundColorSpan(weightColor),start,end,0);
        /******************Workout Unit*******************/
        spanBuilder.append(unit);
        start = end;
        end = spanBuilder.length();
        /******************Workout Sets x Reps*******************/
        buffer = '\n'+Integer.toString(workout.getMaxSets());
        spanBuilder.append(buffer);
        start = end;
        end = spanBuilder.length();
        spanBuilder.setSpan(new ForegroundColorSpan(weightColor),start,end,0);

        spanBuilder.append(" X ");
        start = end;
        end = spanBuilder.length();
        spanBuilder.setSpan(new RelativeSizeSpan(0.7f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        buffer = Integer.toString(workout.getMaxReps());
        spanBuilder.append(buffer);
        start = end;
        end = spanBuilder.length();
        spanBuilder.setSpan(new ForegroundColorSpan(weightColor),start,end,0);

        return spanBuilder;
    }


}
