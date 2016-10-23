package com.fahadhd.liftfit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.aigestudio.wheelpicker.WheelPicker;
import com.fahadhd.liftfit.R;

import java.util.ArrayList;
import java.util.List;

//Display a time picker view for selecting how long rest time should be between sets in settings.
public class TimerPickerPreference extends DialogPreference {
    WheelPicker minutes;
    WheelPicker seconds;
    int minute = 1, second;
    String TAG = TimerPickerPreference.class.getSimpleName();

    public TimerPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.time_picker_dialog);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        SharedPreferences sharePref = getSharedPreferences();
        minute = sharePref.getInt(getContext().getString(R.string.time_pref_units_minutes),3);
        second = sharePref.getInt(getContext().getString(R.string.time_pref_units_seconds),0);
        String summary = (second < 10) ? (minute + ":0" + second) :minute + ":" + second;
        setSummary(summary);
        return super.onCreateView(parent);
    }


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        minutes = (WheelPicker) view.findViewById(R.id.time_picker_minutes);
        seconds = (WheelPicker) view.findViewById(R.id.time_picker_seconds);
        minutes.setSelectedItemPosition(minute);
        seconds.setSelectedItemPosition(second);

            List<Integer> data = new ArrayList<>();
            for (int i = 0; i <= 20; i++)
                data.add(i);
            minutes.setData(data);

            data = new ArrayList<>();
            for (int i = 0; i <= 59; i++)
                data.add(i);
            seconds.setData(data);

            minutes.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                   minute = (Integer)data;
                }
            });
           seconds.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                second = (Integer)data;
            }
        });
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            String summary = (second < 10) ? (minute + ":0" + second) :minute + ":" + second;
            setSummary(summary);
            SharedPreferences.Editor editor = getEditor();
            editor.putInt(getContext().getString(R.string.time_pref_units_minutes), minute);
            editor.putInt(getContext().getString(R.string.time_pref_units_seconds), second);
            editor.commit();
        }
    }




}
