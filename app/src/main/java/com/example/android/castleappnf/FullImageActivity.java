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
//Implements both OnTouchListener and OnGestureListener, both are required to implement slide functionality
public class FullImageActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private static final String TAG = "FullImageActivity";
    //UI elements
    ImageView imageView;
    Toolbar toolbar;
    //Variables to hold the data passed to the activity, i.e. which castle
    Castles myCastle;
    String castleName;
    //Reference to firebase
    private StorageReference mStorageRef;
    //Variables to hold the current image and the total number of images to help cycling through the images
    int currentImage = 1;
    final int maxImages = 3;
    //GestureDetector variable to detect gestures on the screen
    private GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        //Sets up the UI elements
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(castleName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = findViewById(R.id.fullImageView);
        //Gets the firebase reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Get the Castles object from the intent so we know which caslte we are interested in
        myCastle = (Castles) getIntent().getSerializableExtra("Castle");
        castleName = getIntent().getStringExtra("CastleName");
        currentImage = getIntent().getIntExtra("CurrentImage", 1);
        //Creates the gesture detector and assigns it to a variable
        gestureDetector = new GestureDetector(this, this);
        //Gets the Storage Reference to the specific castle image required first
        StorageReference myImage = mStorageRef.child("images/" + castleName.toLowerCase() + " " + currentImage + ".PNG");

        //Gets the image from firebase storage
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
        //Sets the OnTouchListener (which is the activity) to the image view so it responds to touches.
        imageView.setOnTouchListener(this);
    }

    //Helper method to load the image from the uri obtained from Firebase into the image views.
    private void loadImages(Uri uri) {
        if (uri != null) {
            Log.d(TAG, "loadImages: " + uri);
            Picasso.get().load(uri).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(imageView);
        } else {
            Log.d(TAG, "loadImages: " + uri);
            Picasso.get().load(R.drawable.castlethumbnail).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(320, 240).centerInside().into(imageView);
        }
    }

    //When a motion event happens then the motionevent is sent to the gesture detector object.
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    //These methods are all required by we do not implement a behaviour for them.
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

    //Method which cycles through the images for the castle when the user slides the image.
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d(TAG, "onFling: " + "v" + v + "v1:" + v1);
        //If the user slides forwards then this is triggered
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

        }
        //If the user slides backwards then this is triggered.
        else {
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

    //Action defined for when user clicks the back button on the toolbar. This loads the DetailsActivity class and passes the Castles object back
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //When the back button is pressed, i.e. on the phone itself, then the functionality is the same as if the back button on the toolbar is clicked.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("Castle", myCastle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
