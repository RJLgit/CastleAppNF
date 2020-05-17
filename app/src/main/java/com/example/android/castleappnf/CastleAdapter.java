package com.example.android.castleappnf;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;

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
        }
        return new CastleViewHolder(view, mListener);

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
        Collections.sort(castles);
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

        holder.bind(castles.get(position).getName(), castles.get(position).getImage()[0], dist, distanceUnit);
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

        public CastleViewHolder(@NonNull View v, OnRecyclerItemClickListener listener) {
            super(v);
            nameTextView = v.findViewById(R.id.castleNameTextView);
            distanceTextView = v.findViewById(R.id.distanceValueTextView);
            imgView = v.findViewById(R.id.castleRvImageView);
            onRecyclerItemClickListener = listener;
            v.setOnClickListener(this);

        }

        public void bind(String x, int y, float z, String distUnit) {
            nameTextView.setText(x);
            if (distUnit.equals("Km")) {
                int myDist = (int) z / 1000;
                distanceTextView.setText(String.valueOf(myDist) + "KM");
            } else {
                int myDist = (int) z / 1609;
                distanceTextView.setText(String.valueOf(myDist) + "Miles");
            }
            imgView.setImageResource(y);
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
