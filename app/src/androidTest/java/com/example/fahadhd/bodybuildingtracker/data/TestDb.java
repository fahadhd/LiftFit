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
        //mContext.deleteDatabase(TrackerDbHelper.DATABASE_NAME);

    }

    public void testCreateDb() throws Throwable{
        TrackerDAO dao = new TrackerDAO(mContext);
        assertTrue(dao.db.isOpen());
        long sesKey = dao.addSession("June 5", 185);

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
