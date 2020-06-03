package com.example.android.castleappnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FullImageActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    ImageView imageView;
    private StorageReference mStorageRef;
    private static final String TAG = "FullImageActivity";
    int currentImage = 1;
    private GestureDetector gestureDetector;
    final int maxImages = 3;
    String castleName;
    Toolbar toolbar;
    Castles myCastle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        imageView = findViewById(R.id.fullImageView);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        myCastle = (Castles) getIntent().getSerializableExtra("Castle");
        castleName = getIntent().getStringExtra("CastleName");
        gestureDetector = new GestureDetector(this, this);
        StorageReference myImage = mStorageRef.child("images/" + castleName.toLowerCase() + " " + currentImage + ".PNG");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(castleName);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        imageView.setOnTouchListener(this);

    }

    public void loadImages(Uri uri) {
        if (uri != null) {
            Log.d(TAG, "loadImages: " + uri);
            Picasso.get().load(uri).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(imageView);

        } else {

            Log.d(TAG, "loadImages: " + uri);
            Picasso.get().load(R.drawable.castlethumbnail).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(imageView);

        }

    }



   /* @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("Castle", myCastle);
        startActivity(intent);
    }*/



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        gestureDetector.onTouchEvent(motionEvent);

            
        return true;


    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        Log.d(TAG, "onShowPress: ");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Log.d(TAG, "onSingleTapUp: ");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d(TAG, "onScroll: " + v + v1);

        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Log.d(TAG, "onLongPress: ");
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d(TAG, "onFling: " + "v" + v + "v1:" + v1);
        if (v < 0) {
            currentImage++;
            if (currentImage > maxImages) {
                currentImage = 1;
            }
            StorageReference myClickImage = mStorageRef.child("images/" + castleName.toLowerCase() + " " + currentImage + ".PNG");
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

        } else {
            currentImage--;
            if (currentImage < 1) {
                currentImage = maxImages;
            }
            StorageReference myClickImage = mStorageRef.child("images/" + castleName.toLowerCase() + " " + currentImage + ".PNG");
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
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("Castle", myCastle);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("Castle", myCastle);
        startActivity(intent);
    }
}
