package com.example.visualock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {

    private List<String> imageNames;
    private List<String> imageUrls;
    private List<Boolean> toggles;
    private RecyclerView recyclerView, recyclerDefaultView;

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
        toggles = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerDefaultView = findViewById(R.id.recyclerDefaultView);
        recyclerDefaultView.setLayoutManager(new LinearLayoutManager(this));

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

        // Check if the user is an admin
        checkAdminStatus();
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
                            // Add default toggle state
                            toggles.add(false);
                            // Notify the adapter about the new data
                            recyclerDefaultView.setAdapter(new ImageAdapter(StorageActivity.this, imageNames, imageUrls, toggles, false));
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

    private void checkAdminStatus() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users").document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Boolean isAdmin = documentSnapshot.getBoolean("isAdmin");
                            System.out.println("ADMIN "+ isAdmin);
                            if (isAdmin != null && isAdmin) {
                                // User is an admin, show the delete button
                                // Pass isAdmin status to the adapter
                                recyclerDefaultView.setAdapter(new ImageAdapter(StorageActivity.this, imageNames, imageUrls, toggles, true));
                            } else {
                                // User is not an admin, hide the delete button
                                // Pass isAdmin status to the adapter
                                recyclerDefaultView.setAdapter(new ImageAdapter(StorageActivity.this, imageNames, imageUrls, toggles, false));
                            }
                        }
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

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

        private Context context;
        private List<String> imageNames;
        private List<String> imageUrls;
        private List<Boolean> toggles;
        private boolean isAdmin;

        public ImageAdapter(Context context, List<String> imageNames, List<String> imageUrls, List<Boolean> toggles, boolean isAdmin) {
            this.context = context;
            this.imageNames = imageNames;
            this.imageUrls = imageUrls;
            this.toggles = toggles;
            this.isAdmin = isAdmin;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            // Load image into ImageView using Picasso
            Picasso.get().load(imageUrls.get(position)).into(holder.imageView);

            // Set image name
            String imageName = imageNames.get(position);
            if (imageName.length() > 16) {
                // If the name is longer than 16 characters, truncate it
                imageName = imageName.substring(0, 13) + "...";
            }
            holder.textViewName.setText(imageName);

            // Set toggle state
            holder.materialSwitch.setChecked(toggles.get(position));

            // Show/hide delete button based on admin status
            if (isAdmin) {
                holder.deleteButton.setVisibility(View.VISIBLE);
                // Set toggle button alignment to start
                holder.materialSwitch.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                ));
            } else {
                holder.deleteButton.setVisibility(View.GONE);
                // Set toggle button alignment to end
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                holder.materialSwitch.setLayoutParams(params);
            }

            // Delete button click listener
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.materialSwitch.isChecked()) {
                        // If toggle is on, show a dialog
                        showToggleAlertDialog();
                    } else {
                        // If toggle is off, proceed with deletion
                        removeItem(holder.getAdapterPosition());
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return imageNames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textViewName;
            ImageView deleteButton;
            SwitchMaterial materialSwitch;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                textViewName = itemView.findViewById(R.id.textViewName);
                deleteButton = itemView.findViewById(R.id.deleteButton);
                materialSwitch = itemView.findViewById(R.id.material_switch);
            }
        }

        private void removeItem(int position) {
            // Remove the image name, URL, and toggle state from the lists
            imageNames.remove(position);
            imageUrls.remove(position);
            toggles.remove(position);

            // Notify adapter about the removal
            notifyDataSetChanged();
        }

        private void showToggleAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Warning");
            builder.setMessage("Toggle is ON which is your current password \nPlease turn off the toggle switch before deleting.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int
                        which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
