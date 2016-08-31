package com.example.fahadhd.bodybuildingtracker.exercises;



import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.utilities.Constants;


public class TemplateDialog extends DialogFragment implements View.OnClickListener{
    TextView message;
    boolean isTemplateEmpty = false;
    String templateName;
    Button templateCancel, templateConfirm, templateClear;


    public static TemplateDialog newInstance(boolean templateEmpty, String templateName) {
        TemplateDialog newDialog = new TemplateDialog();

        Bundle args = new Bundle();
        args.putBoolean(Constants.TEMPLATE_TASK.TEMPLATE_EMPTY_KEY,templateEmpty);
        args.putString(Constants.TEMPLATE_TASK.TEMPLATE_NAME,templateName);
        newDialog.setArguments(args);

        return newDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(Constants.TEMPLATE_TASK.TEMPLATE_NAME)){
            templateName  = bundle.getString(Constants.TEMPLATE_TASK.TEMPLATE_NAME);
        }
        if(bundle != null && bundle.containsKey(Constants.TEMPLATE_TASK.TEMPLATE_EMPTY_KEY)){
            isTemplateEmpty = bundle.getBoolean(Constants.TEMPLATE_TASK.TEMPLATE_EMPTY_KEY);
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
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        message = (TextView) rootView.findViewById(R.id.template_message);
        templateConfirm = (Button) rootView.findViewById(R.id.template_confirm);
        templateCancel = (Button) rootView.findViewById(R.id.template_cancel);
        templateClear = (Button) rootView.findViewById(R.id.template_clear);

        templateConfirm.setOnClickListener(this);
        templateCancel.setOnClickListener(this);
        templateClear.setOnClickListener(this);


        if(isTemplateEmpty){
            message.setText("Template "+ templateName +" is empty. Would you like to save this " +
                    "session as a template?");
        }
        else{
            templateClear.setVisibility(View.VISIBLE);
            message.setText("Load Template "+ templateName +"?");
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.template_cancel) dismiss();
        if(isTemplateEmpty) setupEmptyTemplate(v);
        else setupUpExistingTemplate(v);
    }



    public void setupEmptyTemplate(View v){
        switch (v.getId()){
            case R.id.template_confirm:
                Intent intent = new Intent();
                intent.putExtra(Constants.TEMPLATE_TASK.SAVE_TEMPLATE,true);
                intent.putExtra(Constants.TEMPLATE_TASK.TEMPLATE_NAME,templateName);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), ExercisesFragment.DIALOG_REQUEST_CODE, intent);
                dismiss();
        }
    }

    public void setupUpExistingTemplate(View v){

        switch (v.getId()){
            //Communicating back to fragment to update workouts to templates
            case R.id.template_confirm:
                Intent loadIntent = new Intent();
                loadIntent.putExtra(Constants.TEMPLATE_TASK.LOAD_TEMPLATE,true);
                loadIntent.putExtra(Constants.TEMPLATE_TASK.TEMPLATE_NAME,templateName);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), ExercisesFragment.DIALOG_REQUEST_CODE, loadIntent);
                dismiss();
                break;
            case R.id.template_clear:
                Intent clearIntent = new Intent();
                clearIntent.putExtra(Constants.TEMPLATE_TASK.CLEAR_TEMPLATE,true);
                clearIntent.putExtra(Constants.TEMPLATE_TASK.TEMPLATE_NAME,templateName);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(), ExercisesFragment.DIALOG_REQUEST_CODE, clearIntent);
                dismiss();
                break;
        }

    }
}
