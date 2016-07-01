package com.example.fahadhd.bodybuildingtracker.Exercises;


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

import com.example.fahadhd.bodybuildingtracker.R;

public class WorkoutDialog extends DialogFragment implements View.OnClickListener{
    EditText workout_name, lift_weight;
    Button sets,reps, cancel, confirm;
    @Override
    public void onStart() {
        super.onStart();
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
        confirm = (Button)rootView.findViewById(R.id.dialog_confirm);

        workout_name.setOnClickListener(this);
        lift_weight.setOnClickListener(this);
        sets.setOnClickListener(this);
        reps.setOnClickListener(this);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);

        manageEditTextCursor(workout_name);
        manageEditTextCursor(lift_weight);



        //setCancelable(false);
        return rootView;
    }

    public void manageEditTextCursor(final EditText iEditText){
        iEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                iEditText.setCursorVisible(false);
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(iEditText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.workout_name){
           
            workout_name.setCursorVisible(true);
        }
        if(v.getId() == R.id.lift_weight){
            lift_weight.setCursorVisible(true);
        }
        if(v.getId() == R.id.dialog_cancel){
            dismiss();
        }
        if(v.getId() == R.id.dialog_confirm){
            dismiss();
        }


    }

    interface Communicator{
        public void getUserWorkout(Workout workout);
    }
}
