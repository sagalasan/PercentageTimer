package com.sagalasan.percentagetimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Christiaan on 3/28/2015.
 */
public class MyDBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "timers.db";
    public static final String  TABLE_TIMERS = "timers";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMERNAME = "timername";
    public static final String COLUMN_STARTDATE = "startdate";
    public static final String COLUMN_ENDDATE = "enddate";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_TIMERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TIMERNAME + " TEXT, " +
                COLUMN_STARTDATE + " INTEGER, " +
                COLUMN_ENDDATE + " INTEGER" + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMERS);
        onCreate(db);
    }

    public void addTimer(Timers t)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMERNAME, t.get_timername());
        values.put(COLUMN_STARTDATE, t.get_startdate());
        values.put(COLUMN_ENDDATE, t.get_enddate());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TIMERS, null, values);
        db.close();
    }

    public void deleteTimer(String timerName)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TIMERS + " WHERE " + COLUMN_TIMERNAME + "=\"" + timerName + "\";");
    }

    public ArrayList<Timers> returnTimers()
    {
        ArrayList<Timers> result = new ArrayList<Timers>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TIMERS + " WHERE 1";
        String timerName = "";
        long sDate = 0;
        long fDate = 0;

        Timers temp;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast())
        {
            temp = new Timers();
            if(c.getString(c.getColumnIndex(COLUMN_TIMERNAME)) != null)
            {
                timerName = c.getString(c.getColumnIndex(COLUMN_TIMERNAME));
                temp.set_timername(timerName);
            }

            sDate = c.getLong(c.getColumnIndex(COLUMN_STARTDATE));
            temp.set_startdate(sDate);

            fDate = c.getLong(c.getColumnIndex(COLUMN_ENDDATE));
            temp.set_enddate(fDate);

            result.add(temp);

            c.moveToNext();
        }
        db.close();
        c.close();

        return result;
    }

    public boolean exists(String n)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT 1 FROM " + TABLE_TIMERS + " WHERE " + COLUMN_TIMERNAME + "=\"" + n + "\"", null);
        boolean exists = (c.getCount() > 0);
        db.close();
        c.close();
        return exists;
    }
}
