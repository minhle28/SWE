package com.example.visualock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StorageActivity extends AppCompatActivity {

    private int[] allImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        ImageView backButton = findViewById(R.id.backButton);
        this.setTitle("Storage");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDashboardFragment();
            }
        });

        // Initialize your arrays
        allImages = new int[] {
                // Add all your image arrays here
                R.drawable.grey, R.drawable.pink, R.drawable.green, R.drawable.orange, R.drawable.yellow,
                R.drawable.blue, R.drawable.black, R.drawable.red, R.drawable.purple
        };

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageAdapter adapter = new ImageAdapter(this, allImages);
        recyclerView.setAdapter(adapter);
    }

    private void navigateToDashboardFragment() {
        Intent intent = new Intent(StorageActivity.this, MainActivity.class);
        intent.putExtra("dashboardFragment", true);
        startActivity(intent);
        finish();
    }
}
