package com.fahadhd.liftfit.exercises;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fahadhd.liftfit.R;

public class WorkoutViewHolder {
    public TextView completed_dialog;
    public TextView workoutInfo;
    public TextView buttonOne,buttonTwo,buttonThree,buttonFour;
    public TextView buttonFive,buttonSix,buttonSeven,buttonEight;
    ImageButton editWorkout;
    //TODO: ADD third set underneath second.
    public WorkoutViewHolder(View view){
        workoutInfo = (TextView) view.findViewById(R.id.workout_info);

        /*********Workout Buttons***********/
        buttonOne = (TextView) view.findViewById(R.id.workout_button_one);
        buttonTwo = (TextView)view.findViewById(R.id.workout_button_two);
        buttonThree = (TextView) view.findViewById(R.id.workout_button_three);
        buttonFour = (TextView) view.findViewById(R.id.workout_button_four);
        buttonFive = (TextView) view.findViewById(R.id.workout_button_five);
        buttonSix = (TextView) view.findViewById(R.id.workout_button_six);
        buttonSeven = (TextView) view.findViewById(R.id.workout_button_seven);
        buttonEight = (TextView) view.findViewById(R.id.workout_button_eight);
        editWorkout = (ImageButton) view.findViewById(R.id.edit_workout);

    }
}