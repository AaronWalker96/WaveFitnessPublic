package com.wave.fitness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rogalabs.lib.model.SocialUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Date;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DashboardActivity extends AppCompatActivity implements Animation.AnimationListener {

    /* Class that creates the Dashboard */

    private HamburgerMenu menu;

    private boolean killOnNext = false;

    Toolbar toolbar;
    SharedPreferences prefs = null;
    CircleImageView profileImage;

    Animation animFadein;

    Gson gson;
    SpotifyCore core;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        core = ((SpotifyCore)getApplicationContext());
        gson = new Gson();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = getSharedPreferences("com.wave.fitness", MODE_PRIVATE);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);

        if(prefs.getBoolean("firstrun", true)){
            startActivity(new Intent(DashboardActivity.this, setupActivity.class));
        }

        for(SpotifyPlaylists.Genre genre : SpotifyPlaylists.Genre.values()){
            core.selectedGenre.put(genre, prefs.getBoolean(genre.name(), false));
        }
    }

    public void updateWellcomeMsg(){
        SimpleDateFormat parseFormat = new SimpleDateFormat("EEEE");
        Date date =new Date();
        String dashboardDate = parseFormat.format(date);

        Random prefix = new Random();
        String[] prefixs = new String[] { "Happy ", "It's ",
                "Don't you just love ", "Let's get you through ", "Welcome to "};

        String prefixString = "Happy ";

        int INDEXn = prefix.nextInt(prefixs.length);
        for (int i2 = 0; i2 < INDEXn; i2++) {
            prefixString = (String) (prefixs[INDEXn]);
        }

        Random endSent = new Random();
        String[] endSentence = new String[] {"!", "!", "?", ".", "."};

        String endSentString = "!";

        for (int i2 = 0; i2 < INDEXn; i2++) {
            endSentString = (String) (endSentence[INDEXn]);
        }

        TextView curDate = (TextView)findViewById(R.id.dashDate);
        curDate.setText(prefixString + dashboardDate + ", " + core.firstName + endSentString);
        updateProfilePic();

    }

    public void createHamburgerMenu(){
        menu = new HamburgerMenu(this, gson.fromJson(prefs.getString("user", ""), SocialUser.class), toolbar);
        core.user = gson.fromJson(prefs.getString("user", ""), SocialUser.class);
        String[] name = core.user.getName().split(" ", 2);
        core.firstName = name[0];
        updateWellcomeMsg();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            Intent firstTime = new Intent(DashboardActivity.this, setupActivity.class);
            DashboardActivity.this.startActivityForResult(firstTime, 22);
        }
        else{
            createHamburgerMenu();

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        createHamburgerMenu();

    }

    public void onRunButtonClicked(View view) {
        startActivity(new Intent(DashboardActivity.this, genreSelection.class));
    }

    @Override
    public void onBackPressed() {
        if(menu.menu.isDrawerOpen()){
            menu.menu.closeDrawer();
        }else {
            if(!killOnNext){
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG).show();
                killOnNext = true;
            }else {
                finishAndRemoveTask();
            }
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for fade in animation
        if (animation == animFadein) {
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    //Intents to start different activities within main class.


    public void onMusicStart() {
        Intent a = new Intent(this, fragmentBaseActivity.class);
        a.putExtra("frgToLoad", "FRAGMENT_A");

        // Now start your activity
        startActivity(a);
    }
    public void onMapsStart() {
        Intent a = new Intent(this, fragmentBaseActivity.class);
        a.putExtra("frgToLoad", "FRAGMENT_A");

        // Now start your activity
        startActivity(a);
    }
    public void onStatsStart() {
        Intent a = new Intent(this, fragmentBaseActivity.class);
        a.putExtra("frgToLoad", "FRAGMENT_C");

        // Now start your activity
        startActivity(a);
    }

    public void startARun(View view) {
        core.dashboardCard = false;
        startActivity(new Intent(DashboardActivity.this, genreSelection.class));
    }

    public void getMoving(View view) {
        core.dashboardCard = true;
        core.chosenPlaylist = "spotify:user:spotify:playlist:37i9dQZF1DX1gcrZ1xC96D";
        startActivity(new Intent(DashboardActivity.this, fragmentBaseActivity.class));
    }

    public void edSheeran(View view) {
        core.dashboardCard = true;
        core.chosenPlaylist = "spotify:album:3T4tUhGYeRNVUGevb0wThu";
        startActivity(new Intent(DashboardActivity.this, fragmentBaseActivity.class));
    }

    public void cleanBandit(View view) {
        core.dashboardCard = true;
        core.chosenPlaylist = "spotify:album:4b13SJlne61y53KSEwuQtD";
        startActivity(new Intent(DashboardActivity.this, fragmentBaseActivity.class));
    }

    public void massiveDanceHits(View view) {
        core.dashboardCard = true;
        core.chosenPlaylist = "spotify:user:ministryofsounduk:playlist:7FUhHHA0zXAPVsJdDrNxNs";
        startActivity(new Intent(DashboardActivity.this, fragmentBaseActivity.class));
    }

    public void teenPopParty(View view) {
        core.dashboardCard = true;
        core.chosenPlaylist = "spotify:user:spotify:playlist:37i9dQZF1DX9bAf4c66TGs";
        startActivity(new Intent(DashboardActivity.this, fragmentBaseActivity.class));
    }

    public void noisesAfterDark(View view) {
        core.dashboardCard = true;
        core.chosenPlaylist = "spotify:user:spotify:playlist:37i9dQZF1DX4fxf4OrMhXb";
        startActivity(new Intent(DashboardActivity.this, fragmentBaseActivity.class));
    }

    private void updateProfilePic(){
        Picasso.with(this)
                .load(core.user.getPhotoUrl())
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        // really ugly darkening trick
                        final Bitmap copy = source.copy(source.getConfig(), true);
                        source.recycle();
                        final Canvas canvas = new Canvas(copy);
                        //canvas.drawColor(0xbb000000);
                        return copy;
                    }

                    @Override
                    public String key() {
                        return "darken";
                    }
                })
                .into(profileImage);
    }
}
