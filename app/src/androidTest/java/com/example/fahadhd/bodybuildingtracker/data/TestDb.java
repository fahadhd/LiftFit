package com.example.fahadhd.bodybuildingtracker.data;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;


public class TestDb extends AndroidTestCase{
    public static final String TAG = TestDb.class.getSimpleName();



    @Override
    public void setUp() throws Exception {
        super.setUp();
        mContext.deleteDatabase(TrackerDbHelper.DATABASE_NAME);

    }

    public void testCreateDb() throws Throwable{
        TrackerDbHelper mDbHelper = new TrackerDbHelper(mContext);
        SQLiteDatabase db;

        db = mDbHelper.getReadableDatabase();
        assertTrue(db.isOpen());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
