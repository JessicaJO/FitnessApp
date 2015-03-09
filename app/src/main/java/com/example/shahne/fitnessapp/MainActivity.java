package com.example.shahne.fitnessapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import static java.security.AccessController.getContext;


public class MainActivity extends ParentActivity {
    private static int rowPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_main);

        new DatabaseRetrieval(this).execute(rowPos);

    }

    /* When a title is clicked, open up the workout */
    public void open(View v) {
        TextView text = (TextView) v;
        Intent intent = new Intent(this, WorkoutActivity.class);
        intent.putExtra("workout", text.getText());
        startActivity(intent);
    }

    public void getNextWorkouts(View view){
        rowPos += 10;
        new DatabaseRetrieval(this).execute(rowPos);

    }


}
