package com.example.shahne.fitnessapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by Shahne on 17/02/2015.
 */
public class WorkoutActivity extends ParentActivity{
    private static final String ERROR = "Error - title not found";
    private VideoAccessories player;
    private DatabaseRetrieval db;
    private SurfaceHolder holder;
    private boolean isReady = false;

    @Override
    public void onCreate(Bundle bundle){ //TODO
        super.onCreate(bundle);

        String title = ERROR;
        Bundle extra = getIntent().getExtras();
        if (extra != null)
            title = (String) extra.get("workout");

        setContentView(R.layout.activity_workout);

        SurfaceView sv = (SurfaceView) findViewById(R.id.video_file);
        holder = sv.getHolder();

        if (!title.equals(ERROR)){
            db = new DatabaseRetrieval(this, title);
            db.execute(1);
        }

    }

    @Override
    public void setUri(Uri uri){
        try {
            SeekBar seekBar = (SeekBar) findViewById(R.id.progressbar);
            player = new VideoAccessories(this, uri, holder, seekBar);
            seekBar.setOnSeekBarChangeListener(player);
        } catch (Exception e){
            Log.e("HELLO", e.getMessage());
            alertUser("There has been an error loading the video. Please try a different workout.");

        }
    }

    @Override
    public void addActivities(int[] t, String[] e, TextView v){
        isReady = true;
        if (player != null)
            player.addActivities(t, e, v);
    }

    public void pause(View view){
        if (player == null || !isReady){
            alertUser("The video is loading. Sorry for the delay");
            return;
        }
        player.changeState();
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

    public void updateProgress(View v){
        if (player != null){
            player.updateProgress();
        }
    }
}
