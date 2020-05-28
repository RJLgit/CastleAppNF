package com.example.android.castleappnf;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CastleAdapter extends RecyclerView.Adapter<CastleAdapter.CastleViewHolder> {

    private Context mContext;
    private ArrayList<Castles> castles;
    private OnRecyclerItemClickListener mListener;
    private Location phoneLocation;
    private String distanceUnit;
    private String sortBy;
    private int lastPosition = -1;
    private View myParent;
    private StorageReference storageReference;
    private Uri image;
    private static final String TAG = "CastleAdapter";


    public CastleAdapter() {
    }

    public CastleAdapter(Context mContext, ArrayList<Castles> castles, OnRecyclerItemClickListener listener, Location location, String distance, String sort, StorageReference ref) {
        this.mContext = mContext;
        this.castles = castles;
        this.mListener = listener;
        this.phoneLocation = location;
        this.distanceUnit = distance;
        this.sortBy = sort;
        storageReference = ref;

    }



    @NonNull
    @Override
    public CastleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        if (Resources.getSystem().getDisplayMetrics().widthPixels > 1000) {
            view = inflater.inflate(R.layout.large_list_item_layout, parent, false);
        }



        myParent = view;
        if (phoneLocation != null && sortBy.equals("Distance")) {
            sortCastlesByDistance();
        } else if (sortBy.equals("A-Z")) {
            sortCastlesByAZ();
        } else if (sortBy.equals("Rating")) {
            sortCastlesByRating();
        }

        return new CastleViewHolder(view, mListener);

    }

    private void sortCastlesByRating() {
        Collections.sort(castles, new Castles.RatingComparator());
    }

    private void sortCastlesByDistance() {
        for (Castles c : castles) {
            float dist = 0;
            Location castLocation = new Location("");
            castLocation.setLongitude(c.getLongdi());
            castLocation.setLatitude(c.getLat());
            dist = phoneLocation.distanceTo(castLocation);
            c.setDistance(dist);

        }
        Collections.sort(castles, new Castles.DistanceComparator());
    }

    private void sortCastlesByAZ() {

        Collections.sort(castles, new Castles.AZComparator());
    }


    @Override
    public void onBindViewHolder(@NonNull final CastleViewHolder holder, final int position) {
        /*Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
       myParent.startAnimation(animation);
        lastPosition = position;*/


        StorageReference myImage = storageReference.child("images/" + castles.get(position).getName().toLowerCase() + " 1.PNG");
        Log.d(TAG, "onBindViewHolder: " + myImage);
        myImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                int animToUse = R.anim.load_down_anim;

                if (position > lastPosition) {
                    animToUse = R.anim.load_up_anim;
                }
                lastPosition = position;

                float dist = 0;
                Location castLocation = new Location("");
                castLocation.setLongitude(castles.get(position).getLongdi());
                castLocation.setLatitude(castles.get(position).getLat());
                if (phoneLocation != null) {
                    dist = phoneLocation.distanceTo(castLocation);
                }

                    holder.bind(castles.get(position).getName(), uri, dist, distanceUnit, castles.get(position).getRating(), mContext, animToUse);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                int animToUse = R.anim.load_down_anim;

                if (position > lastPosition) {
                    animToUse = R.anim.load_up_anim;
                }
                lastPosition = position;

                float dist = 0;
                Location castLocation = new Location("");
                castLocation.setLongitude(castles.get(position).getLongdi());
                castLocation.setLatitude(castles.get(position).getLat());
                if (phoneLocation != null) {
                    dist = phoneLocation.distanceTo(castLocation);
                }
                holder.bind(castles.get(position).getName(), null, dist, distanceUnit, castles.get(position).getRating(), mContext, animToUse);
            }
        });
        //loads correct animation for if scrolling up or down, passes this anim to view holder

    }

    @Override
    public int getItemCount() {
        return castles.size();
    }

    public class CastleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;
        TextView distanceTextView;
        ImageView imgView;
        OnRecyclerItemClickListener onRecyclerItemClickListener;
        RatingBar mRatingBar;
        ConstraintLayout mConstraint;

        public CastleViewHolder(@NonNull View v, OnRecyclerItemClickListener listener) {
            super(v);
            nameTextView = v.findViewById(R.id.castleNameTextView);
            distanceTextView = v.findViewById(R.id.distanceValueTextView);
            imgView = v.findViewById(R.id.castleRvImageView);
            onRecyclerItemClickListener = listener;
            v.setOnClickListener(this);
            mRatingBar = v.findViewById(R.id.myRatingBar);
            mConstraint = v.findViewById(R.id.itemview_parent);
        }

        public void bind(String x, Uri y, float z, String distUnit, int rating, Context context, int theAnim) {
            //applies the anim to the whole viewholder
            mConstraint.setAnimation(AnimationUtils.loadAnimation(context, theAnim));

            nameTextView.setText(x);
            mRatingBar.setRating(rating);
            if (distUnit.equals("Km")) {
                int myDist = (int) z / 1000;
                distanceTextView.setText(String.valueOf(myDist) + " KMs away");
            } else {
                int myDist = (int) z / 1609;
                distanceTextView.setText(String.valueOf(myDist) + " Miles away");
            }
            Log.d(TAG, "bind: " + y);
            if (y != null) {
                Picasso.get().load(y).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(450, 310).centerInside().into(imgView);
            } else {
                Picasso.get().load(R.drawable.ic_error).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(450, 310).centerInside().into(imgView);

            }


        }

        @Override
        public void onClick(View view) {
            onRecyclerItemClickListener.onMyItemClicked(castles.get(getAdapterPosition()));
        }
    }
    public interface OnRecyclerItemClickListener{
        void onMyItemClicked(Castles c);
    }
}
