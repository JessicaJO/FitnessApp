    package com.example.shahne.fitnessapp;

    import android.app.Activity;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.media.MediaPlayer;
    import android.net.Uri;
    import android.os.AsyncTask;
    import android.util.Log;
    import android.view.SurfaceHolder;
    import android.view.SurfaceView;
    import android.widget.TextView;
    import android.widget.VideoView;

    /**
    * Created by Shahne on 6/02/2015.
    */
    public class DatabaseRetrieval  extends AsyncTask<Integer, Void, Void> {
    private DatabaseHelper dbhelper;
    private TextView[] views;
    private String workoutTitle;
    private Uri uri;
    private ParentActivity activity;
    private String[] exercises;
    private int[] times;

    public DatabaseRetrieval(MainActivity activity){
       super();
       this.dbhelper = new DatabaseHelper(activity);
       TextView[] views = {(TextView) activity.findViewById(R.id.workout_title_1), //
                (TextView) activity.findViewById(R.id.workout_user_1), //
                (TextView) activity.findViewById(R.id.workout_title_2), //
                (TextView) activity.findViewById(R.id.workout_user_2), //
                (TextView) activity.findViewById(R.id.workout_title_3), //
                (TextView) activity.findViewById(R.id.workout_user_3), //
                (TextView) activity.findViewById(R.id.workout_title_4), //
                (TextView) activity.findViewById(R.id.workout_user_4), //
                (TextView) activity.findViewById(R.id.workout_title_5), //
                (TextView) activity.findViewById(R.id.workout_user_5), //
                (TextView) activity.findViewById(R.id.workout_title_6), //
                (TextView) activity.findViewById(R.id.workout_user_6), //
                (TextView) activity.findViewById(R.id.workout_title_7), //
                (TextView) activity.findViewById(R.id.workout_user_7), //
                (TextView) activity.findViewById(R.id.workout_title_8), //
                (TextView) activity.findViewById(R.id.workout_user_8), //
                (TextView) activity.findViewById(R.id.workout_title_9), //
                (TextView) activity.findViewById(R.id.workout_user_9), //
                (TextView) activity.findViewById(R.id.workout_title_10), //
                (TextView) activity.findViewById(R.id.workout_user_10)};
       this.views = views;
       workoutTitle = null;
    }

    public DatabaseRetrieval(WorkoutActivity activity, String title){
        super();
        this.dbhelper = new DatabaseHelper(activity);
        this.activity = activity;
        this.workoutTitle = title;
        this.views = new TextView[1];
        this.views[0] = (TextView) activity.findViewById(R.id.activity_display);
    }

    protected Void doInBackground(Integer... start) {
        if (workoutTitle != null)
            getWorkout();
        else
            getTenWorkouts(start[0]);
        return null;
    }

    private void getWorkout(){
       SQLiteDatabase db = dbhelper.getReadableDatabase();

       String[] projection = {WorkoutContract.Video.COLUMN_NAME_VIDEO_Location,
                                WorkoutContract.Video.COLUMN_NAME_VIDEO_ID};
       String where = WorkoutContract.Video.COLUMN_NAME_WORKOUT_TITLE + "=?";
       String[] selection = {workoutTitle};

       Cursor c = db.query(
                    WorkoutContract.Video.TABLE_NAME,
                    projection,
                    where,
                    selection,
                    null,
                    null,
                    null,
                    null
       );

       c.moveToFirst();

       uri = Uri.parse(c.getString(0));

       getActivities(db, c.getString(1));



    }

    private void getActivities(SQLiteDatabase db, String id){
        String[] projection = { WorkoutContract.Activity.COLUMN_NAME_ACTIVITY_TIME,
                WorkoutContract.Activity.COLUMN_NAME_ACTIVITY_NAME};
        String[] selection = {id};
        String where = WorkoutContract.Activity.COLUMN_NAME_VIDEO_ID + "=?";

        Cursor c = db.query(
                WorkoutContract.Activity.TABLE_NAME,
                projection,
                where,
                selection,
                null,
                null,
                WorkoutContract.Activity.COLUMN_NAME_ACTIVITY_TIME
        );

        c.moveToFirst();
        exercises = new String[c.getCount()];
        times = new int[c.getCount()];
        for (int i =  0; i < c.getCount(); i++){
            times[i] = Integer.parseInt(c.getString(0));
            exercises[i] = c.getString(1);
            c.moveToNext();
        }


    } //TODO check

    public void onPostExecute(Void v){
        super.onPostExecute(v);

        if (uri != null)
            activity.setUri(uri);
        if (times != null && exercises != null)
            activity.addActivities(times, exercises, views[0]);
    }

    private void getTenWorkouts(int start){
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        String[] projection = {WorkoutContract.Video.COLUMN_NAME_WORKOUT_TITLE,
                               WorkoutContract.Video.COLUMN_NAME_USER};
        Cursor c = db.query(
                WorkoutContract.Video.TABLE_NAME,  // The table to query
                projection, // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        int i = 0;
        c.moveToPosition(start);
        while (i < 10 && ! c.isAfterLast()){
            views[i].setText(c.getString(0));
            views[i++].setTextSize(15);

            views[i].setText(c.getString(1));
            views[i].setTextSize(15);
            c.moveToNext();
            i++;
        }

    }


    }
