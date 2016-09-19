package com.example.fahadhd.bodybuildingtracker.exercises;

import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.utilities.Constants;

//Dialog that starts when a new exercises is started. This dialog asks users if they want
//to load a template or a new workout. Dialog can be completely skipped and load into
// a template automatically or a new workout dialog by setting options in settings.

public class StartUpExerciseDialog extends DialogFragment implements View.OnClickListener {
    Button loadTemplateA,loadTemplateB;
    Button loadCancel, loadNewWorkout;
    boolean templateAEmpty, templateBEmpty;

    public static StartUpExerciseDialog newInstance( boolean templateChecks[] ) {
        StartUpExerciseDialog newDialog = new StartUpExerciseDialog();
        Bundle args = new Bundle();
        args.putBooleanArray(Constants.TEMPLATE_TASK.TEMPLATE_EMPTY_KEY,templateChecks);
        newDialog.setArguments(args);

        return newDialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if(bundle.containsKey(Constants.TEMPLATE_TASK.TEMPLATE_EMPTY_KEY)){
            boolean templateChecks[] = bundle.getBooleanArray(Constants.TEMPLATE_TASK.TEMPLATE_EMPTY_KEY);
            if(templateChecks != null) {
                templateAEmpty = templateChecks[0];
                templateBEmpty = templateChecks[1];
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_exercise_dialog_fragment, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loadTemplateA = (Button)rootView.findViewById(R.id.load_template_a);
        loadTemplateB = (Button)rootView.findViewById(R.id.load_template_b);
        loadCancel = (Button)rootView.findViewById(R.id.load_cancel);
        loadNewWorkout= (Button)rootView.findViewById(R.id.load_new_workout);

        loadTemplateA.setOnClickListener(this);
        loadTemplateB.setOnClickListener(this);
        loadCancel.setOnClickListener(this);
        loadNewWorkout.setOnClickListener(this);

        if(templateAEmpty) loadTemplateA.setVisibility(View.GONE);
        if(templateBEmpty) loadTemplateB.setVisibility(View.GONE);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent loadIntent;
        switch (v.getId()){
            case R.id.load_template_a:
                loadIntent = new Intent();
                loadIntent.putExtra(Constants.TEMPLATE_TASK.LOAD_TEMPLATE,true);
                loadIntent.putExtra(Constants.TEMPLATE_TASK.TEMPLATE_NAME,getString(R.string.template_A));
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), ExercisesFragment.DIALOG_REQUEST_CODE, loadIntent);
                dismiss();
                break;
            case R.id.load_template_b:
                loadIntent = new Intent();
                loadIntent.putExtra(Constants.TEMPLATE_TASK.LOAD_TEMPLATE,true);
                loadIntent.putExtra(Constants.TEMPLATE_TASK.TEMPLATE_NAME,getString(R.string.template_B));
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), ExercisesFragment.DIALOG_REQUEST_CODE, loadIntent);
                dismiss();
                break;
            case R.id.load_new_workout:
                loadIntent = new Intent();
                loadIntent.putExtra(Constants.GENERAL.NEW_WORKOUT_DIALOG,true);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), ExercisesFragment.DIALOG_REQUEST_CODE, loadIntent);
                dismiss();
                break;
            case R.id.load_cancel:
                dismiss();
                break;
        }

    }




}
