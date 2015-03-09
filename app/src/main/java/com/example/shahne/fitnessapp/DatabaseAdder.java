package com.example.shahne.fitnessapp;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

/**
 * Created by Shahne on 6/02/2015.
 */
public class DatabaseAdder extends AsyncTask<String, String, String> {
    private static DatabaseHelper dbhelper;
    private static CreateActivity context;

    public DatabaseAdder(Context context){
        super();

        this.context = (CreateActivity) context;

        dbhelper = new DatabaseHelper(context);
    }

    protected String doInBackground(String... strings){
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        if (strings.length >= 3) {
            ContentValues values = new ContentValues();
            values.put(WorkoutContract.Video.COLUMN_NAME_WORKOUT_TITLE, strings[0]);
            values.put(WorkoutContract.Video.COLUMN_NAME_USER, strings[1]);
            values.put(WorkoutContract.Video.COLUMN_NAME_VIDEO_Location, strings[2]);

            long rowID = db.insert(WorkoutContract.Video.TABLE_NAME, null, values);
            if (rowID == -1)
                return "Failure";

            String id = Long.toString(rowID);
            int i = 3;
            //While there are activities, add these
            while (i < strings.length-1){
                values = new ContentValues();
                int time = Integer.parseInt(strings[i++]);
                values.put(WorkoutContract.Activity.COLUMN_NAME_ACTIVITY_TIME, time);
                values.put(WorkoutContract.Activity.COLUMN_NAME_ACTIVITY_NAME, strings[i++]);
                values.put(WorkoutContract.Activity.COLUMN_NAME_VIDEO_ID, id);

                rowID = db.insert(WorkoutContract.Activity.TABLE_NAME, null, values);
                if (rowID == -1)
                    return "Failure";
            }

            return "Success";
        }
        return "Failure";
    }

    public void onPostExecute(String result){
        if (result.equals("Success")){
            context.finish();
        } else {
            context.alertUser("There was a problem saving this workout." +
                    " Please verify the inputs and try again.");
        }
    }

}
