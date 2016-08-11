package com.example.fahadhd.bodybuildingtracker.exercises;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.R;

public class WorkoutViewHolder {
    public TextView completed_dialog;
    public TextView workoutInfo;
    public ImageButton buttonOne,buttonTwo,buttonThree,buttonFour;
    public ImageButton buttonFive,buttonSix,buttonSeven,buttonEight;
    //TODO: ADD third set underneath second.
    LinearLayout setOne,setTwo,setThree;
    public WorkoutViewHolder(View view){
        setOne = (LinearLayout) view.findViewById(R.id.button_container_set_1);
        setTwo = (LinearLayout) view.findViewById(R.id.button_container_set_2);
        completed_dialog = (TextView) view.findViewById(R.id.workout_complete_dialog);
        workoutInfo = (TextView) view.findViewById(R.id.workout_info);

        /*********Workout Buttons***********/
        buttonOne = (ImageButton) view.findViewById(R.id.workout_button_one);
        buttonTwo = (ImageButton) view.findViewById(R.id.workout_button_two);
        buttonThree = (ImageButton) view.findViewById(R.id.workout_button_three);
        buttonFour = (ImageButton) view.findViewById(R.id.workout_button_four);
        buttonFive = (ImageButton) view.findViewById(R.id.workout_button_five);
        buttonSix = (ImageButton) view.findViewById(R.id.workout_button_six);
        buttonSeven = (ImageButton) view.findViewById(R.id.workout_button_seven);
        buttonEight = (ImageButton) view.findViewById(R.id.workout_button_eight);

    }
}