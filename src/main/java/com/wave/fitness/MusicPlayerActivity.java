/*
    Wave Fitness Version 1.0
    This is version 1.0 of Wave's Spotify implementation. It utilises the Spotify Android SDK, and it's example project.
    This is merely a foundation to the app, and should be heavily adopted and changed.
    ~ Josh Cawthorne.
    IMPORTANT: DUE TO A BUG WITH THE SPOTIFY SDK, IF A USER HAS THE SPOTIFY APP INSTALLED ON THEIR PHONE, AND THEY TRY TO SIGN IN,
    AUTHENTICATION WILL FAIL.
 */
package com.wave.fitness;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Connectivity;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.PlaybackBitrate;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity implements
        Player.NotificationCallback, ConnectionStateCallback{

    //Constants

    //Required to make the app work - this is our public key and callback URL!
    //Also included suppress warnings.
    @SuppressWarnings("SpellCheckingInspection")
    private static final String CLIENT_ID = "0389348b1134489d870dc8730a7fe33a";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String REDIRECT_URI = "testschema://callback";

    public String TEST_PLAYLIST_URI = "";

    //// TODO: 03/05/17 Create an array of liked/disliked songs via metadata, and then compare new songs to the array. If the song was previously disliked, skip it.

    //// TODO: Toggle menu label on login, so that when a user is logged in, to then say "logout" and visaversa

    //// TODO: Work out how to toggle fab icon on genre select.

    //Generate bool for genre switch
    boolean genreSwitchResume = false;

    //Request code that will be passed together with authentication result to the onAuthenticationResult
    private static final int REQUEST_CODE = 1337;

    //These are UI controls, which can only be used after a user has logged into spotify.
    private static final int[] REQUIRES_INITIALIZED_STATE = {
            R.id.pause_button,
            R.id.genre_switch_button,
    };


    //These are UI controls which can only be used once a song is playing.
    private static final int[] REQUIRES_PLAYING_STATE = {
            R.id.skip_next_button,
            R.id.skip_prev_button,
    };
    public static final String TAG = "SpotifySdkDemo";

    //Fields

    private SpotifyPlayer mPlayer;

    private PlaybackState mCurrentPlaybackState;

    //Used to recieve info on a user's current network status (IE: Wireless, Offline)
    private BroadcastReceiver mNetworkStateReceiver;

    private TextView mMetadataText;

    private EditText mSeekEditText;

    private ScrollView mStatusTextScrollView;
    private Metadata mMetadata;

    Drawer menu;

    public boolean logIn = false;

    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            logStatus("OK!");
            logIn = true;
        }

        @Override
        public void onError(Error error) {
            logStatus("ERROR:" + error);
        }
    };

    //Initialization

    private static boolean mHasItRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);

        if (!mHasItRun) {
            onLoginButtonClicked(null);
            mHasItRun = true;
        }
        else if(mHasItRun) {
            Log.e("MPLAYERSTATE", "UPDATED");
            updateView();
        }

        // Get a reference to any UI widgets that will be needed.
        mMetadataText = (TextView) findViewById(R.id.metadata);
        mStatusTextScrollView = (ScrollView) findViewById(R.id.status_text_container);

        updateView();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.pause_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
                    mPlayer.pause(mOperationCallback);
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.play));
                } else {
                    mPlayer.resume(mOperationCallback);
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                //.withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Josh Cawthorne").withEmail("joshcawthorne97@gmail.com")/*.withIcon(getResources().getDrawable(R.drawable.profile))*/
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        PrimaryDrawerItem music = new PrimaryDrawerItem().withIdentifier(1).withName("Music");

        menu = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        music, new SecondaryDrawerItem().withName("Start A Run"), new SecondaryDrawerItem().withName("Past Runs"),
                        new DividerDrawerItem(), new SecondaryDrawerItem().withName("Settings"), new SecondaryDrawerItem().withName("Logout")
                )
                .withOnDrawerItemClickListener(
                        new Drawer.OnDrawerItemClickListener(){
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                // do something with the clicked item :D
                                startActivity(new Intent(MusicPlayerActivity.this, DashboardActivity.class));
                                return true;
                            }
                        }
                )
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        menu.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //menu.removeItem(R.id.logout);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set up the broadcast receiver for network events.
        mNetworkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mPlayer != null) {
                    Connectivity connectivity = getNetworkConnectivity(getBaseContext());
                    logStatus("Network state changed: " + connectivity.toString());
                    mPlayer.setConnectivityStatus(mOperationCallback, connectivity);
                }
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStateReceiver, filter);

        if (mPlayer != null) {
            mPlayer.addNotificationCallback(MusicPlayerActivity.this);
            mPlayer.addConnectionStateCallback(MusicPlayerActivity.this);
        }
    }

    private Connectivity getNetworkConnectivity(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return Connectivity.fromNetworkType(activeNetwork.getType());
        } else {
            return Connectivity.OFFLINE;
        }
    }

    //Authentication

    private void openLoginWindow() {
        final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
                .build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    onAuthenticationComplete(response);
                    break;

                // Auth flow returned an error
                case ERROR:
                    logStatus("Auth error: " + response.getError());
                    break;
                // Most likely auth flow was cancelled
                default:
                    logStatus("Auth result: " + response.getType());
            }
        }
    }

    private void onAuthenticationComplete(AuthenticationResponse authResponse) {
        // Once we have obtained an authorization token, we can proceed with creating a Player.
        logStatus("Got authentication token");
        if (mPlayer == null) {
            Config playerConfig = new Config(getApplicationContext(), authResponse.getAccessToken(), CLIENT_ID);
            mPlayer = Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                @Override
                public void onInitialized(SpotifyPlayer player) {
                    logStatus("-- Player initialized --");
                    mPlayer.setConnectivityStatus(mOperationCallback, getNetworkConnectivity(MusicPlayerActivity.this));
                    mPlayer.addNotificationCallback(MusicPlayerActivity.this);
                    mPlayer.addConnectionStateCallback(MusicPlayerActivity.this);
                    // Trigger UI refresh
                    updateView();
                }

                @Override
                public void onError(Throwable error) {
                    logStatus("Error in initialization: " + error.getMessage());
                }
            });
        } else {
            mPlayer.login(authResponse.getAccessToken());
        }
    }

    //UI

    private void updateView() {
        boolean loggedIn = isLoggedIn();

        // Login button should be the inverse of the logged in state
        //Button loginButton = (Button) findViewById(R.id.login_button);
        //loginButton.setText(loggedIn ? R.string.logout_button_label : R.string.login_button_label);

        // Set enabled for all widgets which depend on initialized state
        for (int id : REQUIRES_INITIALIZED_STATE) {
            //    findViewById(id).setEnabled(loggedIn);
        }

        // Same goes for the playing state
        boolean playing = loggedIn && mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying;
        for (int id : REQUIRES_PLAYING_STATE) {
            findViewById(id).setEnabled(playing);
        }

        if (mMetadata != null) {
            findViewById(R.id.skip_next_button).setEnabled(mMetadata.nextTrack != null);
            findViewById(R.id.skip_prev_button).setEnabled(mMetadata.prevTrack != null);
            findViewById(R.id.pause_button).setEnabled(mMetadata.currentTrack != null);
        }

        final ImageView coverArtView = (ImageView) findViewById(R.id.cover_art);
        if (mMetadata != null && mMetadata.currentTrack != null) {
            //Set the metadata from song length to Minutes:Seconds, rather than milliseconds.
            final String durationStr =
                    String.format("\n %02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(mMetadata.currentTrack.durationMs),
                            TimeUnit.MILLISECONDS.toSeconds(mMetadata.currentTrack.durationMs) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mMetadata.currentTrack.durationMs))
                    );
            mMetadataText.setText(mMetadata.currentTrack.name + " - " + mMetadata.currentTrack.artistName + durationStr);
            Picasso.with(this)
                    .load(mMetadata.currentTrack.albumCoverWebUrl)
                    .transform(new Transformation() {
                        @Override
                        public Bitmap transform(Bitmap source) {
                            final Bitmap copy = source.copy(source.getConfig(), true);
                            source.recycle();
                            final Canvas canvas = new Canvas(copy);
                            return copy;
                        }

                        @Override
                        public String key() {
                            return "darken";
                        }
                    })
                    .into(coverArtView);
        } else {
            mMetadataText.setText("You're not playing anything yet! " +
                    "\n" +
                    "Are you signed in?");
            coverArtView.setBackground(null);
        }

    }

    private boolean isLoggedIn() {
        return mPlayer != null && mPlayer.isLoggedIn();
    }

    public void onLoginButtonClicked(View view) {
        if (!isLoggedIn()) {
            logStatus("Logging in");
            openLoginWindow();
            showAlertbox(null);
        } else {
            mPlayer.logout();
        }
    }

    public void onPlayButtonClicked(View view, FloatingActionButton fab) {
        String uri = "spotify:user:spotify:playlist:7MizIujRqHWLFVZAfQ21h4";
        logStatus("Starting playback for " + uri);
        mPlayer.playUri(mOperationCallback, uri, 0, 0);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.pause));
    }

    public void onPauseButtonClicked(View view) {
        if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
            mPlayer.pause(mOperationCallback);
        } else {
            mPlayer.resume(mOperationCallback);
        }
    }

    public void onSkipToPreviousButtonClicked(View view) {
        mPlayer.skipToPrevious(mOperationCallback);
    }

    public void onSkipToNextButtonClicked(View view) {
        mPlayer.skipToNext(mOperationCallback);
    }


    public void onToggleShuffleButtonClicked(View view) {

    }

    public void onGenreButtonClicked(View view) {
        //Array that contains all potential playlists.
        String[] playlists = {"spotify:user:4joshua-cawthorne:playlist:4vr1l7iKUXfsmxEFlQabwG",
                "spotify:user:spotify:playlist:37i9dQZF1DX6VdMW310YC7"};
        //Create random
        Random random = new Random();
        //Pause Music if user is playing some.
        if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
            onPauseButtonClicked(view);
            genreSwitchResume = true;
        }
        //Set genre to be random selection from above array
        int index = random.nextInt(playlists.length);
        TEST_PLAYLIST_URI = playlists[index];

        if(genreSwitchResume = true){
            onPauseButtonClicked(view);
            mPlayer.playUri(mOperationCallback, TEST_PLAYLIST_URI, 0, 0);
        }
        Log.e("CREATION", playlists[index]);
    }

    AlertDialog alert;
    String selectedFromList = "null";

    final public void showAlertbox(View view) {
        {String names[] ={"Pop","Classical","Electronic","Funk"};
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MusicPlayerActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.musicplayer_list, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("Choose a genre:");
            final ListView lv = (ListView) convertView.findViewById(R.id.listView1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
            lv.setAdapter(adapter);
            alert = alertDialog.create();
            alert.show();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                    selectedFromList =(String) (lv.getItemAtPosition(myItemInt));
                    Log.e("PLAYLISTS", selectedFromList);
                    setGenre();
                    alert.dismiss();
                }
            });
        }
    }

    public void toggleFab(FloatingActionButton fab) {
        fab.setImageDrawable(getResources().getDrawable(R.drawable.pause));
    }

    public void setGenre() {
        if (selectedFromList == "Pop") {
            String[] popGenre = {
                    "spotify:user:spotify:playlist:37i9dQZF1DWY4lFlS4Pnso",
                    "spotify:user:spotify:playlist:37i9dQZF1DWSVtp02hITpN",
                    "spotify:user:spotify:playlist:37i9dQZF1DXdc6Ams1C6tL",
            };
            //Create random
            Random random = new Random();
            //Pause Music if user is playing some.
            if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
                genreSwitchResume = true;
            }
            //Set genre to be random selection from above array
            int index = random.nextInt(popGenre.length);
            TEST_PLAYLIST_URI = popGenre[index];

            if(genreSwitchResume = true){
                mPlayer.playUri(mOperationCallback, TEST_PLAYLIST_URI, 0, 0);
            }
        }
        else if (selectedFromList == "Classical") {
            String[] classicalGenre = {
                    "spotify:user:spotify:playlist:7MizIujRqHWLFVZAfQ21h4",
                    "spotify:user:spotify:playlist:37i9dQZF1DX561TxkFttR4",
                    "spotify:user:spotify:playlist:37i9dQZF1DXah8e1pvF5oE",
            };
            //Create random
            Random random = new Random();
            //Pause Music if user is playing some.
            if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
                genreSwitchResume = true;
            }
            //Set genre to be random selection from above array
            int index = random.nextInt(classicalGenre.length);
            TEST_PLAYLIST_URI = classicalGenre[index];

            if(genreSwitchResume = true){
                mPlayer.playUri(mOperationCallback, TEST_PLAYLIST_URI, 0, 0);
            }

        }
        else if(selectedFromList == "Electronic") {
            String[] electronicGenre = {
                    "spotify:user:spotify:playlist:37i9dQZF1DX5uokaTN4FTR",
                    "spotify:user:spotify:playlist:37i9dQZF1DWSqPHam7LOqC",
                    "spotify:user:spotify:playlist:37i9dQZF1DWSrVdvTl1tVY",
            };
            //Create random
            Random random = new Random();
            //Pause Music if user is playing some.
            if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
                genreSwitchResume = true;
            }
            //Set genre to be random selection from above array
            int index = random.nextInt(electronicGenre.length);
            TEST_PLAYLIST_URI = electronicGenre[index];

            if(genreSwitchResume = true){
                mPlayer.playUri(mOperationCallback, TEST_PLAYLIST_URI, 0, 0);
            }
        }
        else if(selectedFromList == "Funk") {
            String[] funkyGenre = {
                    "spotify:user:spotify:playlist:37i9dQZF1DX23YPJntYMnh",
                    "spotify:user:spotify:playlist:37i9dQZF1DX6drTZKzZwSo",
                    "spotify:user:spotify:playlist:37i9dQZF1DWSrVdvTl1tVY",
            };
            //Create random
            Random random = new Random();
            //Pause Music if user is playing some.
            if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
                genreSwitchResume = true;
            }
            //Set genre to be random selection from above array
            int index = random.nextInt(funkyGenre.length);
            TEST_PLAYLIST_URI = funkyGenre[index];

            if(genreSwitchResume = true){
                mPlayer.playUri(mOperationCallback, TEST_PLAYLIST_URI, 0, 0);
            }
        }
        else if(selectedFromList == "Not") {
            String[] funkGenre = {
                    "spotify:user:spotify:playlist:4Ebgfxsss10NoZbpRq1E06",
                    "spotify:user:spotify:playlist:37i9dQZF1DX23YPJntYMnh",
                    "spotify:user:spotify:playlist:37i9dQZF1DX7Q7o98uPeg1",
            };
            //Create random
            Random random = new Random();
            //Pause Music if user is playing some.
            if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
                genreSwitchResume = true;
            }
            //Set genre to be random selection from above array
            int index = random.nextInt(funkGenre.length);
            TEST_PLAYLIST_URI = funkGenre[index];

            if(genreSwitchResume = true){
                mPlayer.playUri(mOperationCallback, TEST_PLAYLIST_URI, 0, 0);
            }
        }
    }


    public void onToggleRepeatButtonClicked(View view) {
        mPlayer.setRepeat(mOperationCallback, !mCurrentPlaybackState.isRepeating);
    }

    public void onSeekButtonClicked(View view) {
        final Integer seek = Integer.valueOf(mSeekEditText.getText().toString());
        mPlayer.seekToPosition(mOperationCallback, seek);
    }

    public void onLowBitrateButtonPressed(View view) {
        mPlayer.setPlaybackBitrate(mOperationCallback, PlaybackBitrate.BITRATE_LOW);
    }

    public void onNormalBitrateButtonPressed(View view) {
        mPlayer.setPlaybackBitrate(mOperationCallback, PlaybackBitrate.BITRATE_NORMAL);
    }

    public void onHighBitrateButtonPressed(View view) {
        mPlayer.setPlaybackBitrate(mOperationCallback, PlaybackBitrate.BITRATE_HIGH);
    }

    //Callback Methods

    @Override
    public void onLoggedIn() {
        logStatus("Login complete");
        updateView();
    }

    @Override
    public void onLoggedOut() {
        logStatus("Logout complete");
        updateView();
    }

    public void onLoginFailed(Error error) {
        logStatus("Login error "+ error);
    }

    @Override
    public void onTemporaryError() {
        logStatus("Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(final String message) {
        logStatus("Incoming connection message: " + message);
    }

    // Errors and stuff a lot like, but not identical to errors.
    private void logStatus(String status) {

    }

    // Destruction.

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNetworkStateReceiver);

        if (mPlayer != null) {
            mPlayer.removeNotificationCallback(MusicPlayerActivity.this);
            mPlayer.removeConnectionStateCallback(MusicPlayerActivity.this);
        }
    }

    //@Override
    //protected void onDestroy() {
     //   Spotify.destroyPlayer(this);
     //   super.onDestroy();
    //}

    @Override
    public void onPlaybackEvent(PlayerEvent event) {
        logStatus("Event: " + event);
        mCurrentPlaybackState = mPlayer.getPlaybackState();
        mMetadata = mPlayer.getMetadata();
        Log.i(TAG, "Player state: " + mCurrentPlaybackState);
        Log.i(TAG, "Metadata: " + mMetadata);
        updateView();
    }

    @Override
    public void onPlaybackError(Error error) {
        logStatus("Err: " + error);
    }
}