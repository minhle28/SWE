package com.example.visualock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StorageActivity extends AppCompatActivity {

    private List<String> imageNames;
    private List<String> imageUrls;
    private List<Boolean> toggles;
    private RecyclerView recyclerViewPass, recyclerViewDefault,recyclerViewUpload;
    private ImageAdapter imageAdapterPass,imageAdapterDefault,imageAdapterPassUpload;
    private MyBackend myBackend;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        ImageView backButton = findViewById(R.id.backButton);
        this.setTitle("Storage");
        myBackend = new MyBackend();
        myBackend.context = StorageActivity.this;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDashboardFragment();
            }
        });

        imageNames = new ArrayList<>();
        imageUrls = new ArrayList<>();
        toggles = new ArrayList<>();

        if(!myBackend.isUserLogin())
        {
            myBackend.require = "";
            myBackend.input_email = "";
            startActivity(new Intent(StorageActivity.this, GraphLoginActivity.class));
            finish();
        }
        recyclerViewPass = findViewById(R.id.recyclerViewPass);
        recyclerViewPass.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewDefault = findViewById(R.id.recyclerViewDefault);
        recyclerViewDefault.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewUpload = findViewById(R.id.recyclerViewUpload);
        recyclerViewUpload.setLayoutManager(new LinearLayoutManager(this));

        myBackend.getAllDatabase().thenAccept(resuult1 ->{
            imageAdapterPass = new ImageAdapter(StorageActivity.this,myBackend.userData.getImages_pass(),false);;
            imageAdapterDefault = new ImageAdapter(StorageActivity.this,myBackend.defaultImages,false);;
            imageAdapterPassUpload = new ImageAdapter(StorageActivity.this,myBackend.userUploadImages,false);
            recyclerViewPass.setAdapter(imageAdapterPass);
            recyclerViewDefault.setAdapter(imageAdapterDefault);
            recyclerViewUpload.setAdapter(imageAdapterPassUpload);
        });
        //Hide default image view
        TextView defaultImageView = findViewById(R.id.defaultImageView);
        RecyclerView recyclerDefaultView = findViewById(R.id.recyclerViewDefault);
        defaultImageView.setVisibility(View.GONE);
        recyclerDefaultView.setVisibility(View.GONE);

        findViewById(R.id.defaultButton).setOnClickListener(v -> toggleDefaultImageView());
        findViewById(R.id.userButton).setOnClickListener(v -> toggleUserImageView());
        //refreshData();
    }
    private boolean mutexLock = false;
    private void refreshData(){
        if(mutexLock) return;
        mutexLock = true;
        try {
            myBackend.getAllDatabase().thenAccept(resuult1 ->{
                // Check for changes in the folder's contents
                imageAdapterPass.imageUrls = myBackend.userData.getImages_pass();
                imageAdapterDefault.imageUrls = myBackend.defaultImages;
                imageAdapterPassUpload.imageUrls = myBackend.userUploadImages;
                refreshViewOnly();
                mutexLock =false;
            });
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
            mutexLock=false;
        }
    }
    private void refreshViewOnly(){
        imageAdapterPass.notifyDataSetChanged();
        imageAdapterDefault.notifyDataSetChanged();
        imageAdapterPassUpload.notifyDataSetChanged();
    }

    private void toggleDefaultImageView() {
        TextView defaultImageView = findViewById(R.id.defaultImageView);
        RecyclerView recyclerDefaultView = findViewById(R.id.recyclerViewDefault);
        TextView userImageView = findViewById(R.id.userImageView);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUpload);

        defaultImageView.setVisibility(View.VISIBLE);
        recyclerDefaultView.setVisibility(View.VISIBLE);
        userImageView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        refreshData();
    }

    private void toggleUserImageView() {
        TextView defaultImageView = findViewById(R.id.defaultImageView);
        RecyclerView recyclerDefaultView = findViewById(R.id.recyclerViewDefault);
        TextView userImageView = findViewById(R.id.userImageView);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUpload);

        defaultImageView.setVisibility(View.GONE);
        recyclerDefaultView.setVisibility(View.GONE);
        userImageView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        refreshData();
    }

    private void navigateToDashboardFragment() {
        Intent intent = new Intent(StorageActivity.this, MainActivity.class);
        intent.putExtra("dashboardFragment", true);
        startActivity(intent);
        finish();
    }

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textViewName;
            ImageView deleteButton;
            SwitchMaterial materialSwitch;
            String uRI ="";

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                textViewName = itemView.findViewById(R.id.textViewName);
                deleteButton = itemView.findViewById(R.id.deleteButton);
                materialSwitch = itemView.findViewById(R.id.material_switch);
            }
        }
        private Context context;
        public List<String> imageUrls;
        private boolean isAdmin;
        private boolean lock =false;

        public ImageAdapter(Context context,List<String> imageUrls, boolean isAdmin) {
            this.context = context;
            this.imageUrls = imageUrls;
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
            String keyURI = imageUrls.get(position);
            if(holder.imageView == null || !holder.uRI.equals(keyURI)) {
                if(myBackend.mapBitmap.containsKey(keyURI)){
                    holder.imageView.setImageBitmap(myBackend.mapBitmap.get(keyURI));
                }
                else{
                    try{
                        myBackend.mapBitmap.put(keyURI,Picasso.get().load(keyURI).get());
                        holder.imageView.setImageBitmap(myBackend.mapBitmap.get(keyURI));
                    }
                    catch (Exception exception){
                        Picasso.get().load(keyURI).into(holder.imageView);
                    }
                }

                // Set image name
                String imageName = myBackend.getUrlName(keyURI);
                if (imageName.length() > 16) {
                    // If the name is longer than 16 characters, truncate it
                    imageName = imageName.substring(0, 13) + "...";
                }
                holder.textViewName.setText(imageName);
                holder.uRI = imageUrls.get(position);
            }
            // Set toggle state
            holder.materialSwitch.setChecked(myBackend.userData.getImages_pass().contains(keyURI));

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
                        //showToggleAlertDialog();
                    } else {
                        // If toggle is off, proceed with deletion
                        //removeItem(holder.getAdapterPosition());
                    }
                }
            });
            holder.materialSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // prevent action
                    holder.materialSwitch.setChecked(!holder.materialSwitch.isChecked());
                    int numberPass = myBackend.userData.getImages_pass().size();
                    if (holder.materialSwitch.isChecked() && numberPass == 1) {
                        // Remove the last Image in password
                        // active Texual password
                        // Show get new Password box
                    } if(!holder.materialSwitch.isChecked() && numberPass== 5){
                        // Pass list full to ADD
                        Toast.makeText(StorageActivity.this,"Reached 5 pass images",Toast.LENGTH_SHORT).show();
                        //holder.materialSwitch.setChecked(false);
                    }else {
                        if(lock) return;
                        lock=true;
                        myBackend.changePassword(holder.uRI).thenAccept(results1 ->{
                            lock=false;
                            Toast.makeText(StorageActivity.this,myBackend.getMessenge(results1),Toast.LENGTH_SHORT).show();
                            refreshViewOnly();
                        });
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if(imageUrls == null) return 0;
            return imageUrls.size();
        }


    }
}
