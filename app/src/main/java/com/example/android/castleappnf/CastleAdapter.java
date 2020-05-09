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
    private OnRecyclerItemClickListener mListener;


    public CastleAdapter() {
    }

    public CastleAdapter(Context mContext, ArrayList<Castles> castles, OnRecyclerItemClickListener listener) {
        this.mContext = mContext;
        this.castles = castles;
        this.mListener = listener;
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
        holder.bind(castles.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return castles.size();
    }

    public class CastleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;
        OnRecyclerItemClickListener onRecyclerItemClickListener;
        public CastleViewHolder(@NonNull View v, OnRecyclerItemClickListener listener) {
            super(v);
            nameTextView = v.findViewById(R.id.castleNameTextView);
            onRecyclerItemClickListener = listener;
            v.setOnClickListener(this);

        }

        public void bind(String x) {
            nameTextView.setText(x);
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
