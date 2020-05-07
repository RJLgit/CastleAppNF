package com.example.android.castleappnf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CastleAdapter extends RecyclerView.Adapter<CastleAdapter.CastleViewHolder> {

    private Context mContext;
    private ArrayList<Castles> castles;



    public CastleAdapter() {
    }

    public CastleAdapter(Context mContext, ArrayList<Castles> castles) {
        this.mContext = mContext;
        this.castles = castles;
    }

    @NonNull
    @Override
    public CastleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        return new CastleViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CastleViewHolder holder, int position) {
        holder.bind(castles.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return castles.size();
    }

    public class CastleViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        public CastleViewHolder(@NonNull View v) {
            super(v);
            nameTextView = v.findViewById(R.id.castleNameTextView);

        }

        public void bind(String x) {
            nameTextView.setText(x);
        }

    }
}
