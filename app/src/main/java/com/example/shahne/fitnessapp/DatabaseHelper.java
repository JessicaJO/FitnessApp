package com.example.shahne.fitnessapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.AccessControlContext;

/**
 * Created by Shahne on 5/02/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Fitness Database";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_VIDEO_ENTRIES =
            "CREATE TABLE " + WorkoutContract.Video.TABLE_NAME + " (" +
                    WorkoutContract.Video.COLUMN_NAME_VIDEO_ID + " INTEGER PRIMARY KEY," +
                    WorkoutContract.Video.COLUMN_NAME_WORKOUT_TITLE + TEXT_TYPE + COMMA_SEP +
                    WorkoutContract.Video.COLUMN_NAME_VIDEO_Location + TEXT_TYPE + COMMA_SEP +
                    WorkoutContract.Video.COLUMN_NAME_USER + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_VIDEO_ENTRIES =
            "DROP TABLE IF EXISTS " + WorkoutContract.Video.TABLE_NAME;
    public static final String SQL_CREATE_ACTIVITY_ENTRIES =
            "CREATE TABLE " + WorkoutContract.Activity.TABLE_NAME + " (" +
                    WorkoutContract.Activity.COLUMN_NAME_VIDEO_ID +
                    " INTEGER REFERENCES " + WorkoutContract.Video.TABLE_NAME + "("
                    + WorkoutContract.Video.COLUMN_NAME_VIDEO_ID + ")" + COMMA_SEP +
                    WorkoutContract.Activity.COLUMN_NAME_ACTIVITY_NAME + TEXT_TYPE + COMMA_SEP +
                    WorkoutContract.Activity.COLUMN_NAME_ACTIVITY_TIME + " INTEGER" + COMMA_SEP +
                    "PRIMARY KEY (" + WorkoutContract.Activity.COLUMN_NAME_VIDEO_ID + COMMA_SEP
                    + WorkoutContract.Activity.COLUMN_NAME_ACTIVITY_TIME + ")" +
                    " )";

    public static final String SQL_DELETE_ACTIVITY_ENTRIES =
            "DROP TABLE IF EXISTS " + WorkoutContract.Activity.TABLE_NAME;


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_VIDEO_ENTRIES);
        db.execSQL(SQL_CREATE_ACTIVITY_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int old, int newVersion){
        //TODO Something should probably happen
    }

}
