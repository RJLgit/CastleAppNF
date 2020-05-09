package com.example.android.castleappnf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements CastleAdapter.OnRecyclerItemClickListener {

    Toolbar toolbar;

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
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("UK Castles");
        toolbar.setSubtitle("Click Castle to see more info");
        setSupportActionBar(toolbar);


    }

    @Override
    public void onMyItemClicked(int i) {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
    }
}
