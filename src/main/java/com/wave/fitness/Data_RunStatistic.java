package com.wave.fitness;

import android.location.Location;

import com.spotify.sdk.android.player.Metadata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by s6236422 on 02/05/2017.
 */

public class Data_RunStatistic {

    /* Creates database for running stats. */

    // Labels table name
    public static final String TABLE = "RunStatistic";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "date";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_AVRSPEED = "avrspeed";
    public static final String KEY_AVRPACE = "avrpace";
    public static final String KEY_CALORIES = "calories";
    public static final String KEY_STEPPERMIN = "steppermin";
    public static final String KEY_ROUTE = "route";
    public static final String KEY_SONGS = "songs";


    // property help us to keep data
    public int id;
    public long date;
    public long duration;
    public float distance;
    public float avrspeed;
    public int avrpace;
    public int calories;
    public int totalStep;

    //Need to convert to JSON before storing as String
    public List<Location> route;
    public List<Metadata.Track> songs;



}
