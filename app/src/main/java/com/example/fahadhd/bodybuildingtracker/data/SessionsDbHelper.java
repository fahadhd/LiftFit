package com.example.fahadhd.bodybuildingtracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by fahad on 6/25/16.
 */
public class SessionsDbHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "sessions.db";

    public SessionsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /* Inner class that defines the table contents of the sessions table */
    public static final class SessionEntry implements BaseColumns {

        public static final String TABLE_NAME = "sessions";
        // Column with the foreign key into the exercise table.
        public static final String COLUMN_LOC_KEY = "exercise_id";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "date";
        //Current users weight in lbs
        public static final String COLUMN_WEIGHT = "weight";
    }
    /* Inner class that defines the table contents of the exercise activity table */
    public static final class ExerciseEntry implements BaseColumns {

        public static final String TABLE_NAME = "exercise_activity";
        //Routines such as squats, bench-press, etc are stored as strings.
        public static final String COLUMN_ROUTINE = "routine";
        //Current weight of a routine stored as an int
        public static final String COLUMN_ROUTINE_WEIGHT = "routine_weight";
        //How many reps per routine. ie 5x5 squats or 1x8 bench. Stored as a string.
        public static final String COLUMN_ROUTINE_REPS = "reps";
    }

}
