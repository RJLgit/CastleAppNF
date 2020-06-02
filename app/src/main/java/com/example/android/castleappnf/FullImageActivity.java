package com.example.android.castleappnf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FullImageActivity extends AppCompatActivity {

    ImageView imageView;
    private StorageReference mStorageRef;
    private static final String TAG = "FullImageActivity";
    int currentImage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        imageView = findViewById(R.id.fullImageView);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        String castleName = getIntent().getStringExtra("CastleName");

        StorageReference myImage = mStorageRef.child("images/" + castleName.toLowerCase() + " " + currentImage + ".PNG");

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
}
