package com.example.fahadhd.bodybuildingtracker.Exercises;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.Utility;

//Dialog to add a workout to a current session. Sends resulting data to ExerciseActivity.

public class WorkoutDialog extends DialogFragment implements View.OnClickListener{
    EditText workout_name, lift_weight;
    Button sets,reps, cancel, confirm;
    Communicator communicator;
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.workout_dialog_fragment, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        workout_name = (EditText)rootView.findViewById(R.id.workout_name);
        lift_weight = (EditText)rootView.findViewById(R.id.lift_weight);
        sets = (Button)rootView.findViewById(R.id.sets_choice);
        reps = (Button)rootView.findViewById(R.id.reps_choice);
        cancel = (Button)rootView.findViewById(R.id.dialog_cancel);
        confirm = (Button)rootView.findViewById(R.id.dialog_ok);

        workout_name.setOnClickListener(this);
        lift_weight.setOnClickListener(this);
        sets.setOnClickListener(this);
        reps.setOnClickListener(this);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);

        Utility.manageEditTextCursor(workout_name,getActivity());
        Utility.manageEditTextCursor(lift_weight,getActivity());



        //setCancelable(false);
        return rootView;
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.workout_name){
            workout_name.setCursorVisible(true);
            workout_name.setError(null);
        }
        if(v.getId() == R.id.lift_weight){
            lift_weight.setCursorVisible(true);
        }
        if(v.getId() == R.id.dialog_ok){
            String name = workout_name.getText().toString();
            String weightString = lift_weight.getText().toString();
            int weight;
            if(name.equals("")) {
                workout_name.setError("Please Type An Exercise");

            }
            else{
                Toast.makeText(getActivity(),"One or more fields are not filled",
                        Toast.LENGTH_LONG).show();
               // dismiss();
            }

        }
        if(v.getId() == R.id.dialog_cancel){
            dismiss();
        }

    }
    //Used to send information of a new workout to exercise activity
    interface Communicator{
        public void getWorkoutInfo(String name, int weight, int max_sets, int max_reps);
    }

}
