package com.example.android.castleappnf;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends BaseActivity {
    private static final String TAG = "DetailsActivity";
    //UI elements
    Button openTimesButton;
    Button operatedByButton;
    ImageView castleImageView;
    TextView historyTitleTextView;
    TextView historyDetailsTextView;
    Button addressButton;
    TextView ratingTitleTextView;
    RatingBar myRatingBarWidget;
    PlayerView mPlayerView;
    private SimpleExoPlayer player;
    Toolbar toolbar;
    TextView bottStatus;
    BottomNavigationView bottNav;
    ScrollView scrollView;
    ImageView forwards;
    ImageView backwards;


    //Variables associated with the media player
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    //Firebase StorageReference variable
    private StorageReference mStorageRef;

    //Variables to define the current image being displayed and the total number of images for each castle on firebase
    int currentImage = 1;
    //Number of images per castle in firebase
    final int maxImages = 3;

    //Castles object passed to the activity from MainActivity
    Castles myCastle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //Get the firebase instance and reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Get the Castles object in the intent sent to this activity
        Intent intent = getIntent();
        myCastle = (Castles) intent.getSerializableExtra("Castle");
        //Get all the information from that object to populate the Activity views with
        final String name = myCastle.getName();
        final String operator = myCastle.getOperator();
        final String[] history = myCastle.getHistory();
        final int rating = myCastle.getRating();
        final int audio = myCastle.getAudio();
        final String webPage = myCastle.getWebsite();
        final String openTimesWeb = myCastle.getOpeningTimes();
        //A specific reference to the firebase storage image to display first for that castle
        StorageReference myImage = mStorageRef.child("images/" + name.toLowerCase() + " " + currentImage + ".PNG");

        //Defines the UI elements
        bottStatus = findViewById(R.id.bottom_status_text_view);
        bottNav = findViewById(R.id.bott_nav_bar_details);
        scrollView = findViewById(R.id.scroll_view_container);
        openTimesButton = findViewById(R.id.opening_times_button);
        operatedByButton = findViewById(R.id.operated_by_button);
        castleImageView = findViewById(R.id.photos_image_view);
        historyTitleTextView = findViewById(R.id.details_history_title_text_view);
        historyDetailsTextView = findViewById(R.id.history_details_text_view);
        addressButton = findViewById(R.id.address_details_button);
        ratingTitleTextView = findViewById(R.id.site_rating_title);
        myRatingBarWidget = findViewById(R.id.ratingBar);
        mPlayerView = findViewById(R.id.playerView);
        forwards = findViewById(R.id.forwardImage);
        backwards = findViewById(R.id.backwardsImage);

        //Set the buttons to the desired strings and sets the rating bar to be visible
        openTimesButton.setText("Opening times change due to time of year - click to see current opening times");
        historyTitleTextView.setText("Brief history of the site");
        ratingTitleTextView.setText(getString(R.string.details_rating_title));
        myRatingBarWidget.setVisibility(View.VISIBLE);

        for (int i = 0; i < history.length; i++) {
            String string = "\u21AC " + history[i];
            SpannableString spannableString = new SpannableString(string);
            spannableString.setSpan(new RelativeSizeSpan(2f), 0, 1, 0);
            spannableString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 1, 0);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent, getTheme())), 0, 1, 0);
            historyDetailsTextView.append(spannableString);
            historyDetailsTextView.append("\n\n");
        }
        operatedByButton.setText("Operated by: " + operator + ". Click to visit their website.");
        myRatingBarWidget.setRating(rating);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        toolbar.setSubtitle("Here are the details");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        operatedByButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webPage));
                startActivity(browserIntent);
            }
        });

        openTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(openTimesWeb));
                startActivity(browserIntent);
            }
        });

        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri locUri = Uri.parse("geo:" + myCastle.getLat() + "," + myCastle.getLongdi() + "?q=" + myCastle.getLat() + "," + myCastle.getLongdi() + "(" + myCastle.getName() + ")");
                Log.d(TAG, "onClick: " + locUri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, locUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
        castleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImage++;
                if (currentImage > maxImages) {
                    currentImage = 1;
                }
                StorageReference myClickImage = mStorageRef.child("images/" + name.toLowerCase() + " " + currentImage + ".PNG");
                myClickImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        loadImages(uri);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        loadImages(null);
                    }
                });
            }
        });
        forwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImage++;
                if (currentImage > maxImages) {
                    currentImage = 1;
                }
                StorageReference myClickImage = mStorageRef.child("images/" + name.toLowerCase() + " " + currentImage + ".PNG");
                myClickImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        loadImages(uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        loadImages(null);
                    }
                });
            }
        });

        backwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImage--;
                if (currentImage < 1) {
                    currentImage = maxImages;
                }
                StorageReference myClickImage = mStorageRef.child("images/" + name.toLowerCase() + " " + currentImage + ".PNG");
                myClickImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        loadImages(uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        loadImages(null);
                    }
                });
            }
        });
        registerForContextMenu(historyDetailsTextView);
        loadImages(null);

        myImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                loadImages(uri);





            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                loadImages(null);

            }
        });

        //Sets up data in details view - change to use data sent from intent


    }

    public void loadImages(Uri uri) {
        if (uri != null) {
            Log.d(TAG, "loadImages: " + uri);
            Picasso.get().load(uri).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(castleImageView);
            registerForContextMenu(castleImageView);
        } else {

            Log.d(TAG, "loadImages: " + uri);
            Picasso.get().load(R.drawable.castlethumbnail).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(castleImageView);

        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == historyDetailsTextView) {
            getMenuInflater().inflate(R.menu.long_click_share_menu, menu);
        } else if (v == castleImageView) {
            getMenuInflater().inflate(R.menu.long_click_image_menu, menu);
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_item:
                Toast.makeText(this, "Selected share", Toast.LENGTH_SHORT).show();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TITLE, "Some facts about " + toolbar.getTitle().toString());
                shareIntent.putExtra(Intent.EXTRA_TEXT, historyDetailsTextView.getText().toString());
                String x = shareIntent.getStringExtra(Intent.EXTRA_TEXT);
                Log.d(TAG, "onContextItemSelected: " + x);
                startActivity(Intent.createChooser(shareIntent, "Share using"));
                return true;
            case R.id.full_image_item:
                Intent intent = new Intent(this, FullImageActivity.class);
                intent.putExtra("CastleName", myCastle.getName());
                intent.putExtra("Castle", myCastle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Toast.makeText(this, "Open full image activity", Toast.LENGTH_SHORT).show();
            default:
                return super.onContextItemSelected(item);
        }


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
        /*IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, filter);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
        /*unregisterReceiver(connectionReceiver);*/
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

    @Override
    public void showBottomToolBar() {
        Log.d(TAG, "showBottomToolBar: ");
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) scrollView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 60);
        bottNav.setVisibility(View.VISIBLE);
        bottStatus.setText("Offline");
        bottStatus.setBackgroundColor(Color.RED);
    }

    @Override
    public void hideBottomToolBar() {
        Log.d(TAG, "hideBottomToolBar: ");
        if (bottNav.getVisibility() == View.VISIBLE) {
            bottStatus.setText("Online");
            bottStatus.setBackgroundColor(Color.GREEN);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) scrollView.getLayoutParams();
                    layoutParams.setMargins(0, 0, 0, 0);
                    bottNav.setVisibility(View.INVISIBLE);
                }
            }, 5000);
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
