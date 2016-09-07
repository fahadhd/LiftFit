package com.example.fahadhd.bodybuildingtracker.exercises;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.utilities.Utility;

import org.apache.commons.lang3.text.WordUtils;

//Dialog to add a workout to a current session. Sends resulting data to ExerciseActivity.

public class WorkoutDialog extends DialogFragment implements View.OnClickListener{
    EditText workout_name, lift_weight;
    TextView title;
    Button cancel, confirm, delete;
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
        title = (TextView) rootView.findViewById(R.id.workout_dialog_title);
        sets = (Spinner)rootView.findViewById(R.id.sets_choice);
        reps = (Spinner)rootView.findViewById(R.id.reps_choice);
        cancel = (Button)rootView.findViewById(R.id.dialog_cancel);
        confirm = (Button)rootView.findViewById(R.id.dialog_ok);
        delete = (Button)rootView.findViewById(R.id.dialog_delete);

        workout_name.setOnClickListener(this);
        lift_weight.setOnClickListener(this);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
        delete.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.workout_name :
                workout_name.setCursorVisible(true);
                workout_name.setError(null);
                break;
            case R.id.lift_weight :lift_weight.setCursorVisible(true); break;
            case R.id.dialog_ok :
                String name = workout_name.getText().toString();
                String weightString = lift_weight.getText().toString();
                Double weight;
                if (name.equals("")) {
                    workout_name.setError("Please Type An Exercise");
                }
                else {
                    weight = (weightString.equals("")) ? 185 : Double.parseDouble(weightString);
                    if(!Utility.getUnit(getActivity()).equals("LB")) weight = Math.ceil(weight/0.45359237);
                    communicator.addWorkoutInfo(name, weight.intValue(), setChoice, repChoice);
                    dismiss();
                }
                break;
            case R.id.dialog_cancel: dismiss(); break;
        }
    }

    public void setUpExistingWorkout(View v){
        if(currWorkout == null) {
            dismiss();
            return;
        }
        switch (v.getId()){
            case R.id.workout_name :
                workout_name.setCursorVisible(true);
                workout_name.setError(null);
                break;
            case R.id.lift_weight :lift_weight.setCursorVisible(true); break;
            case R.id.dialog_ok :
                String name = workout_name.getText().toString();
                int setSelected = Integer.parseInt((String) sets.getSelectedItem());
                int repSelected = Integer.parseInt((String)reps.getSelectedItem());
                String weightString = lift_weight.getText().toString();
                int weight = (weightString.equals("")) ? 185 : Integer.parseInt(weightString);
                if (name.equals("")) {
                    workout_name.setError("Please Type An Exercise");
                }
                communicator.updateWorkoutInfo(currWorkout,name,weight,setSelected,repSelected);
                dismiss();
                break;
            case R.id.dialog_cancel : dismiss(); break;
            case R.id.dialog_delete:
                if(currWorkout != null)
                    communicator.deleteWorkoutInfo(currWorkout);
                dismiss();
                break;

        }
    }
    //Sets the views to the corresponding workout data
    public void addExistingWorkoutData(){
        Double weight = (double) currWorkout.getWeight();
        delete.setVisibility(View.VISIBLE);
        title.setText("Modify Workout");
        confirm.setText("Update");
        workout_name.setText(WordUtils.capitalizeFully(currWorkout.getName()));

        if(!Utility.getUnit(getActivity()).equals("LB")) weight = Math.floor(weight*0.45359237);

        lift_weight.setText(Integer.toString(weight.intValue()));
        setChoice = currWorkout.getMaxSets();
        repChoice = currWorkout.getMaxReps();
    }

    public void setUpSpinners(){
        String[] setItems = new String[]{"1", "2", "3","4","5","6","7","8"};
        String[] repItems = new String[]{"1", "2", "3","4","5","6","7","8","9","10","11","12"
        ,"13","14","15"};
        ArrayAdapter<String> setAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, setItems);
        ArrayAdapter<String> repAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, repItems);
        sets.setAdapter(setAdapter);
        reps.setAdapter(repAdapter);

        sets.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                workout_name.clearFocus();
                lift_weight.clearFocus();

                return false;
            }
        });

        reps.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                workout_name.clearFocus();
                lift_weight.clearFocus();

                return false;
            }
        });

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
        void addWorkoutInfo(String name, int weight, int max_sets, int max_reps);
        void updateWorkoutInfo(Workout workout, String name, int weight, int max_sets, int max_reps);
        void deleteWorkoutInfo(Workout workout);
    }

}
