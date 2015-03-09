package com.example.shahne.fitnessapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Shahne on 8/02/2015.
 */
public class CreateActivity extends ParentActivity {
    private SurfaceView video;
    private Uri videoLocation;
    private String[] files;
    private Dialog dialog = null;
    private VideoAccessories player;
    private RelativeLayout layout;
    private ArrayList<EditText> activities = new ArrayList<EditText>();
    private int fakeView;
    private String title;
    private String user;
    private SeekBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //TODO reload db onResume
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create);

        getFiles(Environment.getExternalStorageDirectory().toString());

        video = (SurfaceView) findViewById(R.id.video_file);
        layout = (RelativeLayout) findViewById(R.id.exercise);
        activities.add((EditText) findViewById(R.id.exercise_time));
        activities.add((EditText) findViewById(R.id.exercise_activity));
        fakeView = R.id.fakeView;
        bar = (SeekBar) findViewById(R.id.progressbar);
    }


    /*
    * Enables the user to choose a video file
    *  from their SD card.
     */
    public void chooseFile(View view){
        /*Intent intent = new Intent(this, FileChooserActivity.class);
        startActivity(intent);*/
        onCreateDialog();
        dialog.show();
    }

    private void getFiles(String dir){
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        files = new String[cursor.getCount()];// + cursor2.getCount()];
        int i = 0;

        if (!cursor.moveToFirst()) {
            alertUser("No videos could be found");
        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Video.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Video.Media._ID);
            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                files[i++] = thisId + " " + thisTitle;
            } while (cursor.moveToNext());
        }
    }

    private void onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your file");
        builder.setItems(files, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long id = Long.parseLong(files[which].split(" ")[0]);
                videoLocation = ContentUris.withAppendedId(
                        android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                try {
                    player = new VideoAccessories(
                            getBaseContext(),
                            videoLocation,
                            video.getHolder(),
                            bar);
                    bar.setOnSeekBarChangeListener(player);
                } catch (Exception e){
                    alertUser(e.getMessage());
                }

            }
        });
        dialog = builder.create();
        dialog = builder.show();
    }



    public void addTime(View view){
        if (player == null)
            return;
        if (player.getPlayer().isPlaying()) {
            player.changeState();
            String time = Integer.toString(player.getPlayer().getCurrentPosition());
            //Get the last timeVal textView
            EditText timeVal = activities.get(activities.size() - 2);
            //If the last timeVal has no time entered, then use that.
            if (timeVal.getText().toString().equals("")) {
                timeVal.setText(time);
            } else { //add a new exercise pair.
                addTextField(time);
            }
        } else {
            player.changeState();
        }
    }

    public void addTextField(String time){
        EditText text1 = new EditText(this);
        EditText text2 = new EditText(this);

        //The last EditText
        int last = activities.get(activities.size()-1).getId();
        int secondLast = activities.get(activities.size()-2).getId();

        //Add ids to the texts
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            text1.setId(findFreeId(last));
            text2.setId(findFreeId(text1.getId()));
        } else {
            text1.setId(View.generateViewId());
            text2.setId(View.generateViewId());
        }

        //Set text1 to align with left of parent and below the last row
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.BELOW, secondLast);
        params.addRule(InputType.TYPE_CLASS_DATETIME);
        text1.setLayoutParams(params);

        //Set text2 to align with right of fakeview
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, fakeView);
        params.addRule(RelativeLayout.BELOW, last);
        text2.setLayoutParams(params);

        text1.setText(time);
        text2.setHint("Exercise");


        //Add the editText to the relativeLayout
        layout.addView(text1);
        layout.addView(text2);

        //Increase the size of the layout
        int size = layout.getHeight() + 40;
        layout.getLayoutParams().height = size;

        //Add the editText to activities ArrayList
        activities.add(text1);
        activities.add(text2);
    }

    /** Probably a slow method to ensure that a unique id is used */
    private int findFreeId(int start){
        View v;
        do {
            v = findViewById(++start);
        } while (v != null);
        return start;
    }

    /* Verifies that there is a title, owner, video and at least one activity. */
    private boolean verifyInputs(View view){
        EditText titleMod = (EditText) findViewById(R.id.workout_title);
        EditText userMod = (EditText) findViewById(R.id.workout_user);
        EditText activityMod = activities.get(1);
        EditText timeMod = activities.get(activities.size()-1);

        this.title = titleMod.getText().toString();
        this.user = userMod.getText().toString();
        String activity = activityMod.getText().toString();
        String time = timeMod.getText().toString();
        if (title.equals("")) {
            alertUser("A title is needed!");
            return false;
        } else if (user.equals("")) {
            alertUser("The workout must belong to a user");
            return false;
        } else if (player == null){
            alertUser("You must choose a video");
            return false;
        } else if (activity.equals("")){
            alertUser("You must include at least one exercise");
            return false;
        } else if (time.equals("")){
            //Uses the finishing time of the video as the final time.
            timeMod.setText(Integer.toString(player.getPlayer().getDuration()));
        }
        return true;

    }

    /*
     * Verifies the given inputs and then adds the workout
     * to the database.
     */
    public void addWorkout(View view){
        EditText text = (EditText) findViewById(R.id.workout_title);
        if (!verifyInputs(view)){
           return;
        } else { //TODO
            String[] params = getParams();
            new DatabaseAdder(this).execute(params);
            //alertUser("The activity should be saved"); //TODO Activity should be closed after user is alerted.
        }
    }

    /* Converts the activities ArrayList into a String array */
    public String[] getParams(){
        ArrayList<String> params = new ArrayList<String>();
        params.add(title);
        params.add(user);
        params.add(videoLocation.toString());
        while (activities.size() > 0){
            String time = activities.remove(0).getText().toString();
            String activity = activities.remove(0).getText().toString();
            //If no activity or time has been entered, the pair will be ignored.
            if (!activity.equals("") && !time.equals("")){
                params.add(time);
                params.add(activity);
            }
        }
        return params.toArray(new String[params.size()]);
    }

    public void onPause(){
        super.onPause();
        if (player != null && player.getPlayer().isPlaying())
            player.changeState();
    }

    public void onStop(){
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

}
