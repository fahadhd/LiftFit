package com.example.fahadhd.bodybuildingtracker;


import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

//Handles all helper methods for various classes

public class Utility {

    //Determines when the cursor should be shown or not for edit text fields.
    public static void manageEditTextCursor(final EditText iEditText, final Context context){
        iEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                iEditText.setCursorVisible(false);
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(iEditText.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }
}
