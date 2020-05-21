package com.example.android.castleappnf;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CastleAdapter extends RecyclerView.Adapter<CastleAdapter.CastleViewHolder> {

    private Context mContext;
    private ArrayList<Castles> castles;
    private OnRecyclerItemClickListener mListener;
    private Location phoneLocation;
    private String distanceUnit;
    private String sortBy;


    public CastleAdapter() {
    }

    public CastleAdapter(Context mContext, ArrayList<Castles> castles, OnRecyclerItemClickListener listener, Location location, String distance, String sort) {
        this.mContext = mContext;
        this.castles = castles;
        this.mListener = listener;
        this.phoneLocation = location;
        this.distanceUnit = distance;
        this.sortBy = sort;
    }



    @NonNull
    @Override
    public CastleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
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
    public void onBindViewHolder(@NonNull CastleViewHolder holder, int position) {
        float dist = 0;
        Location castLocation = new Location("");
        castLocation.setLongitude(castles.get(position).getLongdi());
        castLocation.setLatitude(castles.get(position).getLat());
        if (phoneLocation != null) {
            dist = phoneLocation.distanceTo(castLocation);
        }

        holder.bind(castles.get(position).getName(), castles.get(position).getImage()[0], dist, distanceUnit, castles.get(position).getRating());
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

        public CastleViewHolder(@NonNull View v, OnRecyclerItemClickListener listener) {
            super(v);
            nameTextView = v.findViewById(R.id.castleNameTextView);
            distanceTextView = v.findViewById(R.id.distanceValueTextView);
            imgView = v.findViewById(R.id.castleRvImageView);
            onRecyclerItemClickListener = listener;
            v.setOnClickListener(this);
            mRatingBar = v.findViewById(R.id.myRatingBar);
        }

        public void bind(String x, int y, float z, String distUnit, int rating) {
            nameTextView.setText(x);
            mRatingBar.setRating(rating);
            if (distUnit.equals("Km")) {
                int myDist = (int) z / 1000;
                distanceTextView.setText(String.valueOf(myDist) + " KM");
            } else {
                int myDist = (int) z / 1609;
                distanceTextView.setText(String.valueOf(myDist) + " Miles");
            }
            Picasso.get().load(y).placeholder(R.drawable.castlethumbnail).error(R.drawable.ic_error).resize(450, 310).centerInside().into(imgView);
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
