package com.example.shahne.fitnessapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.SurfaceHolder;

import android.net.Uri;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.TimerTask;

/**
 * Created by Shahne on 19/02/2015.
 */
public class VideoAccessories implements Runnable, SeekBar.OnSeekBarChangeListener{
    private MediaPlayer player;
    private int[] times;
    private String[] exercises;
    private Handler handler;
    private TextView display;
    private ProgressBar progressBar;

    public VideoAccessories(Context context, Uri videoLocation, SurfaceHolder holder, ProgressBar bar) throws Exception {
        player = new MediaPlayer();
        player.setDataSource(context, videoLocation);
        player.setDisplay(holder);
        player.prepareAsync();

        progressBar = bar;
        handler = new Handler();
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
        if (fromUser && player != null) {
            player.seekTo(progress);
            if (times != null)
                setExercise();
        }
    }

    public void onStartTrackingTouch(SeekBar s){}
    public void onStopTrackingTouch(SeekBar s){}

    public void run() {
        setProgressBar();
        if (times != null && times.length > 0)
            setExercise();
        handler.postDelayed(this, 500);
    }

    /* Shows the video's progress */
    private void setProgressBar() {
        progressBar.setProgress(player.getCurrentPosition());
    }

    public void updateProgress(){
        player.seekTo(progressBar.getProgress());
    }

    /* Displays what activity should be performed */
    private void setExercise() {
        for (int i = 0; i < times.length; i++){
            if (player.getCurrentPosition() <= times[i] || i == times.length-1){
                display.setText(exercises[i]);
                return;
            }
        }
    }

    /* Starts or pauses the video */
    public void changeState(){
        if (player.isPlaying()) {
            player.pause();
            handler.removeCallbacks(this);
        } else {
            player.start();
            progressBar.setMax(player.getDuration());
            handler.postDelayed(this, 500);
        }
    }

    public void addActivities(int[] times, String[] exercises, TextView display) {
        this.times = times;
        this.exercises = exercises;
        this.display = display;
    }

   /* public void pause(){
        if (player.isPlaying())
            player.pause();
    }*/

    public void release(){
        player.release();
        handler.removeCallbacks(this);
    }

}