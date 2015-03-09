package com.example.shahne.fitnessapp;

import android.provider.BaseColumns;

/**
 * Created by Shahne on 31/01/2015.
 */
public final class WorkoutContract {

    public WorkoutContract(){}

    /* A table to hold the location of the video with an id and title */
    public static abstract class Video implements BaseColumns{
        public static final String TABLE_NAME = "video";
        public static final String COLUMN_NAME_VIDEO_ID = "id";
        public static final String COLUMN_NAME_WORKOUT_TITLE = "title";
        public static final String COLUMN_NAME_VIDEO_Location = "location";
        public static final String COLUMN_NAME_USER = "user";
    }

    /* A table to hold a reference to the video, a time point in that video and an
    exercise to do up until that point.
     */
    public static abstract class Activity implements BaseColumns{
        public static final String TABLE_NAME = "activity";
        public static final String COLUMN_NAME_VIDEO_ID = "id";
        public static final String COLUMN_NAME_ACTIVITY_NAME = "exercise";
        public static final String COLUMN_NAME_ACTIVITY_TIME = "time";

    }
}
