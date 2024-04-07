package com.example.visualock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {

    private List<String> imageNames;
    private List<String> imageUrls;
    private ImageAdapter adapter;

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

        imageNames = new ArrayList<>();
        imageUrls = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ImageAdapter(this, imageNames, imageUrls);
        recyclerView.setAdapter(adapter);

        //Hide default image view
        TextView defaultImageView = findViewById(R.id.defaultImageView);
        RecyclerView recyclerDefaultView = findViewById(R.id.recyclerDefaultView);
        defaultImageView.setVisibility(View.GONE);
        recyclerDefaultView.setVisibility(View.GONE);

        Button defaultButton = findViewById(R.id.defaultButton);
        defaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDefaultImageView();
            }
        });

        Button userButton = findViewById(R.id.userButton);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleUserImageView();
            }
        });

        fetchImagesFromFirebaseStorage();
    }

    private void fetchImagesFromFirebaseStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    // Get the name of the image
                    String imageName = item.getName();
                    // Get the download URL of the image
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                        @Override
                        public void onSuccess(android.net.Uri uri) {
                            String imageUrl = uri.toString();
                            // Add the image name and URL to the lists
                            imageNames.add(imageName);
                            imageUrls.add(imageUrl);
                            // Notify the adapter about the new data
                            adapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
            }
        });
    }

    private void toggleDefaultImageView() {
        TextView defaultImageView = findViewById(R.id.defaultImageView);
        RecyclerView recyclerDefaultView = findViewById(R.id.recyclerDefaultView);
        TextView userImageView = findViewById(R.id.userImageView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        defaultImageView.setVisibility(View.VISIBLE);
        recyclerDefaultView.setVisibility(View.VISIBLE);
        userImageView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

    }

    private void toggleUserImageView() {
        TextView defaultImageView = findViewById(R.id.defaultImageView);
        RecyclerView recyclerDefaultView = findViewById(R.id.recyclerDefaultView);
        TextView userImageView = findViewById(R.id.userImageView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);


        defaultImageView.setVisibility(View.GONE);
        recyclerDefaultView.setVisibility(View.GONE);
        userImageView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

    }

    private void navigateToDashboardFragment() {
        Intent intent = new Intent(StorageActivity.this, MainActivity.class);
        intent.putExtra("dashboardFragment", true);
        startActivity(intent);
        finish();
    }
}
