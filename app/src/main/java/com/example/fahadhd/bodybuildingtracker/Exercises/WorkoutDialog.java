package com.example.fahadhd.bodybuildingtracker.exercises;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.utilities.Utility;

//Dialog to add a workout to a current session. Sends resulting data to ExerciseActivity.

public class WorkoutDialog extends DialogFragment implements View.OnClickListener{
    EditText workout_name, lift_weight;
    Button cancel, confirm;
    Spinner sets,reps;
    public static final String TAG = WorkoutDialog.class.getSimpleName();
    public static final String WORKOUT_KEY = "current-workout";
    Communicator communicator;
    boolean editableWorkout = false;
    Workout currWorkout;
    public static int setChoice, repChoice;

    public static WorkoutDialog newInstance(Workout currWorkout) {
        WorkoutDialog newDialog = new WorkoutDialog();

        Bundle args = new Bundle();
        args.putSerializable(WORKOUT_KEY,currWorkout);
        newDialog.setArguments(args);

        return newDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(WORKOUT_KEY)) {
            currWorkout = (Workout) bundle.getSerializable(WORKOUT_KEY);
            editableWorkout = (currWorkout != null);
        }
    }

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
        sets = (Spinner)rootView.findViewById(R.id.sets_choice);
        reps = (Spinner)rootView.findViewById(R.id.reps_choice);
        cancel = (Button)rootView.findViewById(R.id.dialog_cancel);
        confirm = (Button)rootView.findViewById(R.id.dialog_ok);

        workout_name.setOnClickListener(this);
        lift_weight.setOnClickListener(this);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);

        if (editableWorkout) {
            addExistingWorkoutData();
            Log.v(TAG, "workout exists!");
        }

        setUpSpinners();

        Utility.manageEditTextCursor(workout_name, getActivity());
        Utility.manageEditTextCursor(lift_weight,getActivity());

        //setCancelable(false);
        return rootView;
    }



    @Override
    public void onClick(View v) {
        if(editableWorkout){
            setUpExistingWorkout(v);
        }
        else {
            setupNewWorkout(v);
        }
    }

    public void setupNewWorkout(View v){
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
                weight = (weightString.equals("")) ? 185: Integer.parseInt(weightString);
                communicator.getWorkoutInfo(name,weight,setChoice,repChoice);
                dismiss();

            }

        }
        if(v.getId() == R.id.dialog_cancel){
            dismiss();
        }

    }

    public void setUpExistingWorkout(View v){

    }

    //Sets the views to the corresponding workout data
    public void addExistingWorkoutData(){
        workout_name.setText(currWorkout.getName());
        lift_weight.setText(Integer.toString(currWorkout.getWeight()));
        setChoice = currWorkout.getMaxSets();
        repChoice = currWorkout.getMaxReps();
    }

    public void setUpSpinners(){
        String[] items = new String[]{"1", "2", "3","4","5","6","7","8"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, items);
        sets.setAdapter(adapter);
        reps.setAdapter(adapter);

        //The following configures the default choice to be 5 sets by 5 reps.

        setChoice = (editableWorkout) ? currWorkout.getMaxSets() : 5;
        repChoice = (editableWorkout) ? currWorkout.getMaxReps() : 5;

        sets.setSelection(setChoice-1);
        reps.setSelection(repChoice-1);

        sets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setChoice = position+1;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                repChoice = position+1;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Used to send information of a new workout to exercise activity
    interface Communicator{
        void getWorkoutInfo(String name, int weight, int max_sets, int max_reps);
    }

}
