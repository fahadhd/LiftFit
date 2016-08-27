package com.example.fahadhd.bodybuildingtracker.exercises;



import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.R;


public class TemplateDialog extends DialogFragment implements View.OnClickListener{
    TextView message;
    public static final String TEMPLATE_NAME = "template name";
    public static final String TEMPLATE_EMPTY = "template empty";
    public static final String LOAD_TEMPLATE = "load template";
    public static final int DIALOG_REQUEST_CODE = 23;
    boolean templateEmpty = false;
    String templateName;
    Button cancel, confirm;


    public static TemplateDialog newInstance(boolean templateEmpty, String templateName) {
        TemplateDialog newDialog = new TemplateDialog();

        Bundle args = new Bundle();
        args.putBoolean(TEMPLATE_EMPTY,templateEmpty);
        args.putString(TEMPLATE_NAME,templateName);
        newDialog.setArguments(args);

        return newDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(TEMPLATE_NAME)){
            templateName  = bundle.getString(TEMPLATE_NAME);
        }
        if(bundle != null && bundle.containsKey(TEMPLATE_EMPTY)){
            templateEmpty = bundle.getBoolean(TEMPLATE_EMPTY);
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
        View rootView = inflater.inflate(R.layout.template_dialog_fragment, container);
        getDialog().setTitle("Template "+ templateName);

        message = (TextView) rootView.findViewById(R.id.template_message);
        confirm = (Button) rootView.findViewById(R.id.template_confirm);
        cancel = (Button) rootView.findViewById(R.id.template_cancel);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);



        if(templateEmpty){
            message.setText("Template "+ templateName +" is empty. Would you like to save this " +
                    "session as a template?");
        }
        else{
            message.setText("Load template "+ templateName +"?");
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.template_cancel) dismiss();
        if(templateEmpty) setupEmptyTemplate(v);
        else setupUpExistingTemplate(v);
    }



    public void setupEmptyTemplate(View v){
        switch (v.getId()){
            case R.id.template_confirm:

        }
    }

    public void setupUpExistingTemplate(View v){
        switch (v.getId()){
            //Communicating back to fragment to update workouts to templates
            case R.id.template_confirm:
                Intent intent = new Intent();
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), DIALOG_REQUEST_CODE, intent);
                dismiss();
        }

    }



    //Used to send information of a new workout to exercise activity
    interface Communicator{
        void getWorkoutInfo(String name, int weight, int max_sets, int max_reps);
        void updateWorkoutInfo(Workout workout, String name, int weight, int max_sets, int max_reps);
        void deleteWorkoutInfo(Workout workout);
    }

}
