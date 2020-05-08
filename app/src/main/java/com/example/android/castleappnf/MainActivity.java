package com.example.android.castleappnf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements CastleAdapter.OnRecyclerItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        CastleAdapter castleAdapter = new CastleAdapter(this, DummyData.generateAndReturnData(this), this);
        recyclerView.setAdapter(castleAdapter);

    }

    @Override
    public void onMyItemClicked(int i) {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
    }
}
