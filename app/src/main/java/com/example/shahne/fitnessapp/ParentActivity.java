package com.example.shahne.fitnessapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Shahne on 19/02/2015.
 */
public abstract class ParentActivity extends ActionBarActivity {
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
    }

    protected Dialog alertUser(String message){
        Dialog result = null;
        String[] exit = {"Okay"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        builder.setItems(exit, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){}
        });
        result = builder.create();
        result.show();
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case (R.id.action_search):
                return true;
            case (R.id.action_settings):
                return true;
            case (R.id.action_create):
                createWorkout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createWorkout(){
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

    public void setVideo(VideoAccessories v){}
    public void setUri(Uri uri){}
    public void addActivities(int[] t, String[] e, TextView v){}

}
