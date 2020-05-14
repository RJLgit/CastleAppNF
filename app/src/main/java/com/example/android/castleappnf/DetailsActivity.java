package com.example.android.castleappnf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;


public class DetailsActivity extends AppCompatActivity {


    TextView openTimesTextView;
    TextView operatedByTextView;
    ImageView castleImageView;
    TextView historyTitleTextView;
    TextView historyDetailsTextView;
    TextView addressTextView;
    TextView ratingTitleTextView;
    RatingBar myRatingBarWidget;
    PlayerView mPlayerView;
    private SimpleExoPlayer player;
    Toolbar toolbar;
    int resourceId;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Castles myCastle = (Castles) intent.getSerializableExtra("Castle");

        String name = myCastle.getName();
        String operator = myCastle.getOperator();
        String history = myCastle.getHistory();
        int rating = myCastle.getRating();
        int image = myCastle.getImage();
        int audio = myCastle.getAudio();
        final String webPage = myCastle.getWebsite();

        resourceId = audio;



        openTimesTextView = findViewById(R.id.opening_times_text_view);

        operatedByTextView = findViewById(R.id.operated_by_text_view);

        castleImageView = findViewById(R.id.photos_image_view);

        historyTitleTextView = findViewById(R.id.details_history_title_text_view);

        historyDetailsTextView = findViewById(R.id.history_details_text_view);

        addressTextView = findViewById(R.id.address_details_text_view);

        ratingTitleTextView = findViewById(R.id.site_rating_title);

        myRatingBarWidget = findViewById(R.id.ratingBar);
        mPlayerView = findViewById(R.id.playerView);

        //Sets up data in details view - change to use data sent from intent


        castleImageView.setImageDrawable(getResources().getDrawable(image));

        openTimesTextView.setText("Opening times: 9-5 Every day");

        historyTitleTextView.setText("Brief history of the site");


        ratingTitleTextView.setText(getString(R.string.details_rating_title));
        addressTextView.setText(getString(R.string.address_title));

        myRatingBarWidget.setVisibility(View.VISIBLE);
        historyDetailsTextView.setText(history);
        operatedByTextView.setText("Operated by: " + operator);
        myRatingBarWidget.setRating(rating);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        toolbar.setSubtitle("Here are the details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        operatedByTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webPage));
                startActivity(browserIntent);
            }
        });




    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(this);
        mPlayerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        /*Resources resources = this.getResources();
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(resourceId))
                .appendPath(resources.getResourceTypeName(resourceId))
                .appendPath(resources.getResourceEntryName(resourceId))
                .build();
        MediaSource mediaSource = buildMediaSource(uri);*/
        player.prepare(buildMediaSource(resourceId));
    }


    private MediaSource buildMediaSource(int ra) {
        DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(ra));
        final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(this);
        try {
            rawResourceDataSource.open(dataSpec);
        } catch (RawResourceDataSource.RawResourceDataSourceException e) {
            e.printStackTrace();
        }

        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "my-app");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(rawResourceDataSource.getUri());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }



    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
