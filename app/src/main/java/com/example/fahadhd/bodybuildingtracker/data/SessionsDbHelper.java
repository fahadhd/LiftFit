package com.example.fahadhd.bodybuildingtracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class SessionsDbHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "sessions.db";

    public SessionsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold each session of exercise.
        final String SQL_CREATE_SESSIONS_TABLE = "CREATE TABLE " + SessionEntry.TABLE_NAME + " (" +
                SessionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SessionEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                SessionEntry.COLUMN_USER_WEIGHT + " INTEGER NOT NULL, " +
                " );";

        //Each sessions can have 1-5 workouts which are stored in the workouts_table
        final String SQL_CREATE_WORKOUTS_TABLE = "CREATE TABLE " + WorkoutEntry.TABLE_NAME + " (" +
                WorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                WorkoutEntry.COLUMN_SES_KEY + " INTEGER NOT NULL, " +
                WorkoutEntry.COLUMN_WORKOUT_NUM + " INTEGER NOT NULL, " +
                WorkoutEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                WorkoutEntry.COLUMN_WEIGHT + " INTEGER NOT NULL," +
                // Set up the session_id column as a foreign key to session table.
                " FOREIGN KEY (" + WorkoutEntry.COLUMN_SES_KEY + ") REFERENCES " +
                SessionEntry.TABLE_NAME + " (" + SessionEntry._ID + "));";

        //Each workout has a set number of sets and the current number of reps
        //TODO: Add 5 MORE POTENTIAL SETS WHEN FINISHED TESTING
        final String SQL_CREATE_SETS_TABLE = "CREATE TABLE " + SetEntry.TABLE_NAME + " (" +
                SetEntry._ID + " INTEGER PRIMARY KEY," +

                SetEntry.COLUMN_WORK_KEY + " INTEGER NOT NULL, " +
                SetEntry.COLUMN_MAX_SETS + " INTEGER NOT NULL, " +
                SetEntry.COLUMN_MAX_REPS + " INTEGER NOT NULL, " +
                SetEntry.COLUMN_FIRST_SET + " INTEGER NOT NULL DEFAULT -1, " +
                SetEntry.COLUMN_SECOND_SET + " INTEGER NOT NULL DEFAULT -1, " +
                SetEntry.COLUMN_THIRD_SET + " INTEGER NOT NULL DEFAULT -1, " +
                // Set up the workout column as a foreign key to workout table.
                " FOREIGN KEY (" + SetEntry.COLUMN_WORK_KEY + ") REFERENCES " +
                WorkoutEntry.TABLE_NAME + " (" + WorkoutEntry._ID + "));";

        db.execSQL(SQL_CREATE_SESSIONS_TABLE);
        db.execSQL(SQL_CREATE_WORKOUTS_TABLE);
        db.execSQL(SQL_CREATE_SETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /* Inner class that defines the table contents of the sessions table */
    public static final class SessionEntry implements BaseColumns {

        public static final String TABLE_NAME = "sessions";
        public static final String COLUMN_DATE = "date";
        //Current users weight in lbs
        public static final String COLUMN_USER_WEIGHT = "user_weight";
    }
    /* Inner class that defines the table contents of each workout a session includes */
    public static final class WorkoutEntry implements BaseColumns {

        public static final String TABLE_NAME = "workouts";

        public static final String COLUMN_SES_KEY = "session_id";

        public static final String COLUMN_WORKOUT_NUM = "workout_num";

        //Current weight of a routine stored as an int
        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_WEIGHT = "weight";

    }

    /* Inner class that defines the table contents of each set and rep a workout includes */
    public static final class SetEntry implements BaseColumns {

        public static final String TABLE_NAME = "Sets";

        public static final String COLUMN_WORK_KEY = "workout_id";

        public static final String COLUMN_MAX_SETS = "max_sets";

        public static final String COLUMN_MAX_REPS = "max_reps";

        public static final String COLUMN_FIRST_SET = "first_set";

        public static final String COLUMN_SECOND_SET = "second_set";

        public static final String COLUMN_THIRD_SET = "third_set";

    }

}
