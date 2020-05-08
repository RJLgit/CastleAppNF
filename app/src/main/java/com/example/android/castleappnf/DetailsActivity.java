package com.example.android.castleappnf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;


public class DetailsActivity extends AppCompatActivity {
    TextView titleTextView;
    TextView statusTextView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        titleTextView = findViewById(R.id.site_name_text_view);
        statusTextView = findViewById(R.id.status_text_view);
        openTimesTextView = findViewById(R.id.opening_times_text_view);;
        operatedByTextView = findViewById(R.id.operated_by_text_view);;
        castleImageView = findViewById(R.id.photos_image_view);

        historyTitleTextView = findViewById(R.id.details_history_title_text_view);;
        historyDetailsTextView = findViewById(R.id.history_details_text_view);;
        addressTextView = findViewById(R.id.address_details_text_view);;
        ratingTitleTextView = findViewById(R.id.site_rating_title);;
        myRatingBarWidget = findViewById(R.id.ratingBar);
        mPlayerView = findViewById(R.id.playerView);

        //Sets up data in details view - change to use data sent from intent

        titleTextView.setText("test title");
        castleImageView.setImageDrawable(getResources().getDrawable(R.drawable.castle));
        statusTextView.setText(getString(R.string.status_details_title));
        openTimesTextView.setText(getString(R.string.opening_times_title));

        historyTitleTextView.setText(getString(R.string.history_title));


        ratingTitleTextView.setText(getString(R.string.details_rating_title));
        addressTextView.setText(getString(R.string.address_title));

        myRatingBarWidget.setVisibility(View.VISIBLE);
        historyDetailsTextView.setText("This is the history");
        operatedByTextView.setText(getString(R.string.operated_by_title) + "operator");
        myRatingBarWidget.setRating(4);

        initializePlayer();
    }
    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(this);
        mPlayerView.setPlayer(player);
    }
}
