package com.example.android.castleappnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends AppCompatActivity {


    Button openTimesTextView;
    Button operatedByTextView;
    ImageView castleImageView;
    TextView historyTitleTextView;
    TextView historyDetailsTextView;
    Button addressButton;
    TextView ratingTitleTextView;
    RatingBar myRatingBarWidget;
    PlayerView mPlayerView;
    private SimpleExoPlayer player;
    Toolbar toolbar;
    int resourceId;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private static final String TAG = "DetailsActivity";
    private int imgIndex = 0;
    ImageView forwards;
    ImageView backwards;
    private StorageReference mStorageRef;
    int currentImage = 1;
    //Number of images per castle in firebase
    final int maxImages = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        final Castles myCastle = (Castles) intent.getSerializableExtra("Castle");

        final String name = myCastle.getName();
        final String operator = myCastle.getOperator();
        final String[] history = myCastle.getHistory();
        final int rating = myCastle.getRating();
        int image = myCastle.getImage()[imgIndex];
        final int audio = myCastle.getAudio();
        final String webPage = myCastle.getWebsite();
        final String openTimesWeb = myCastle.getOpeningTimes();

        StorageReference myImage = mStorageRef.child("images/" + name.toLowerCase() + " " + currentImage + ".PNG");


        resourceId = audio;



        openTimesTextView = findViewById(R.id.opening_times_text_view);
        operatedByTextView = findViewById(R.id.operated_by_text_view);
        castleImageView = findViewById(R.id.photos_image_view);
        historyTitleTextView = findViewById(R.id.details_history_title_text_view);
        historyDetailsTextView = findViewById(R.id.history_details_text_view);
        addressButton = findViewById(R.id.address_details_text_view);
        ratingTitleTextView = findViewById(R.id.site_rating_title);
        myRatingBarWidget = findViewById(R.id.ratingBar);
        mPlayerView = findViewById(R.id.playerView);
        forwards = findViewById(R.id.forwardImage);
        backwards = findViewById(R.id.backwardsImage);

        myImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(castleImageView);
                openTimesTextView.setText("Opening times change due to time of year - click to see current opening times");
                historyTitleTextView.setText("Brief history of the site");
                ratingTitleTextView.setText(getString(R.string.details_rating_title));
                myRatingBarWidget.setVisibility(View.VISIBLE);
                for (int i = 0; i < history.length; i++) {
                    historyDetailsTextView.append("\u25CF" + history[i]);
                    historyDetailsTextView.append("\n\n");
                }
                //historyDetailsTextView.setText(history);
                operatedByTextView.setText("Operated by: " + operator + ". Click to visit their website.");
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

                openTimesTextView.setOnClickListener(new View.OnClickListener() {
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
                                Picasso.get().load(uri).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(castleImageView);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                                Picasso.get().load(R.drawable.ic_error).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(castleImageView);

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
                                Picasso.get().load(uri).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(castleImageView);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                                Picasso.get().load(R.drawable.ic_error).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(castleImageView);

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
                                Picasso.get().load(uri).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(castleImageView);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                                Picasso.get().load(R.drawable.ic_error).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(castleImageView);

                            }
                        });
                    }
                });

                registerForContextMenu(historyDetailsTextView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Picasso.get().load(R.drawable.ic_error).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(castleImageView);
                openTimesTextView.setText("Opening times change due to time of year - click to see current opening times");
                historyTitleTextView.setText("Brief history of the site");
                ratingTitleTextView.setText(getString(R.string.details_rating_title));
                myRatingBarWidget.setVisibility(View.VISIBLE);
                for (int i = 0; i < history.length; i++) {
                    historyDetailsTextView.append("\u25CF" + history[i]);
                    historyDetailsTextView.append("\n\n");
                }
                //historyDetailsTextView.setText(history);
                operatedByTextView.setText("Operated by: " + operator + ". Click to visit their website.");
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

                openTimesTextView.setOnClickListener(new View.OnClickListener() {
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



                registerForContextMenu(historyDetailsTextView);
            }
        });

        //Sets up data in details view - change to use data sent from intent


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.long_click_share_menu, menu);

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
