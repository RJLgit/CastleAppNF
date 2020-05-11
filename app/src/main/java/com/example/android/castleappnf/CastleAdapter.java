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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CastleAdapter extends RecyclerView.Adapter<CastleAdapter.CastleViewHolder> {

    private Context mContext;
    private ArrayList<Castles> castles;
    private OnRecyclerItemClickListener mListener;
    private Location phoneLocation;


    public CastleAdapter() {
    }

    public CastleAdapter(Context mContext, ArrayList<Castles> castles, OnRecyclerItemClickListener listener, Location location) {
        this.mContext = mContext;
        this.castles = castles;
        this.mListener = listener;
        this.phoneLocation = location;
    }



    @NonNull
    @Override
    public CastleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        return new CastleViewHolder(view, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull CastleViewHolder holder, int position) {
        float dist = 0;
        if (phoneLocation != null) {
            dist = phoneLocation.distanceTo(castles.get(position).getCastleLocation());
        }
        holder.bind(castles.get(position).getName(), castles.get(position).getImage(), dist);
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

        public void bind(String x, int y, float z) {
            nameTextView.setText(x);
            int inMiles = (int) z / 1609;
            distanceTextView.setText(String.valueOf(inMiles));
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
