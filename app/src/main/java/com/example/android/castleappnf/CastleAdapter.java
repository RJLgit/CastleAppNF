package com.example.android.castleappnf;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
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
    //Collection of Castles to populated the recyclerview with
    private ArrayList<Castles> castles;
    //Click listener which is implemented in MainActivity.
    private OnRecyclerItemClickListener mListener;
    //Location of the phone which is set from MainActivity when Location changes by 1 KM at least or when app starts
    private Location phoneLocation;
    //Shared preference info from MainActivity.
    private String distanceUnit;
    private String sortBy;
    private String filterBy;
    //FireBase storage reference
    private StorageReference storageReference;

    private int lastPosition = -1;
    private View myParent;
    private Uri image;

    private static final String TAG = "CastleAdapter";



    public CastleAdapter() {
        //Empty constructor
    }

    //Constructor
    public CastleAdapter(Context mContext, ArrayList<Castles> castlesParam, OnRecyclerItemClickListener listener, String distance, String sort, StorageReference ref, String filter) {
        this.mContext = mContext;
        //The shared preference data from MainActivity
        this.distanceUnit = distance;
        this.sortBy = sort;
        filterBy = filter;
        //Filters the collection of Castles if the user selected to filter the Castles.
        //May have to add more filter options here if more filter options added.
        if (filterBy.equals("English Heritage")) {
            this.castles = filterCastles(castlesParam);
        } else {
            this.castles = castlesParam;
        }
        this.mListener = listener;
        //Receives the Firebase Storage reference in order to get the images
        storageReference = ref;


    }

    //Getter for phone location
    public Location getPhoneLocation() {
        return phoneLocation;
    }

    //Setter for phone location so it can be set (when it changes) in adapter from Mainactivity without recreated whole adapter (as would have to do if sent location in constructor)
    public void setPhoneLocation(Location phoneLocation) {
        this.phoneLocation = phoneLocation;

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

    private ArrayList<Castles> filterCastles(ArrayList<Castles> x) {

        ArrayList<Castles> newList = new ArrayList<>();
        for (Castles c : x) {
            if (c.getOperator().equals("English Heritage")) {
                newList.add(c);
            }
        }
        Log.d(TAG, "filterCastles: " + newList);
        return newList;

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
        int animToUse = R.anim.slide_in_left;

        float dist = 0;
        Location castLocation = new Location("");
        castLocation.setLongitude(castles.get(position).getLongdi());
        castLocation.setLatitude(castles.get(position).getLat());
        if (phoneLocation != null) {
            dist = phoneLocation.distanceTo(castLocation);
        }
        holder.bind(castles.get(position).getName(), dist, distanceUnit, castles.get(position).getRating(), mContext, animToUse);
        /*Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
       myParent.startAnimation(animation);*/
        lastPosition = position;


        StorageReference myImage = storageReference.child("images/" + castles.get(position).getName().toLowerCase() + " 1.PNG");
        Log.d(TAG, "onBindViewHolder: " + myImage);
        myImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
               /* int animToUse = R.anim.load_down_anim;

                if (position > lastPosition) {
                    animToUse = R.anim.load_up_anim;
                }
                lastPosition = position;*/
                holder.setImage(uri);




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                /*int animToUse = R.anim.load_down_anim;

                if (position > lastPosition) {
                    animToUse = R.anim.load_up_anim;
                }
                lastPosition = position;*/
                holder.setImage(null);

            }
        });
        //loads correct animation for if scrolling up or down, passes this anim to view holder

    }

   /* @Override
    public void onBindViewHolder(@NonNull final CastleViewHolder holder, final int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            Log.d(TAG, "onBindViewHolder: empty payloads");
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Log.d(TAG, "onBindViewHolder: payloads");
            float dist = 0;
            Location castLocation = new Location("");
            castLocation.setLongitude(castles.get(position).getLongdi());
            castLocation.setLatitude(castles.get(position).getLat());
            if (phoneLocation != null) {
                dist = phoneLocation.distanceTo(castLocation);
                Log.d(TAG, "onBindViewHolder: payloads " + "position: " + position + "dist: " + dist + "location is " + phoneLocation);
            }
            if (distanceUnit.equals("Km")) {

                int myDist = (int) dist / 1000;
                Log.d(TAG, "onBindViewHolder: payloads " + "position: " + position + "dist: " + myDist);
                holder.distanceTextView.setText(String.valueOf(myDist) + " KMs away");
            } else {
                int myDist = (int) dist / 1609;
                Log.d(TAG, "onBindViewHolder: payloads " + "position: " + position + "dist: " + myDist);
                holder.distanceTextView.setText(String.valueOf(myDist) + " Miles away");
            }
            *//*if (phoneLocation != null && sortBy.equals("Distance")) {
                sortCastlesByDistance();
                Log.d(TAG, "onBindViewHolder: payloads sort" + position);
            }*//*
        }


    }*/

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

        public void bind(String x, float z, String distUnit, int rating, Context context, int theAnim) {
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
            Picasso.get().load(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(450, 310).centerInside().into(imgView);


        }

        public void setImage(Uri y) {
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
