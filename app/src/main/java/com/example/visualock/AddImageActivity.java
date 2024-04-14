package com.example.visualock;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.net.URI;

public class AddImageActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private ImageView imageView;
    private Button selectImageButton;
    private Button uploadImageButton;
    private MyBackend myBackend;
    private Uri pickedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        this.setTitle("Add Image");

        imageView = findViewById(R.id.imageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        ImageView backButton = findViewById(R.id.backButton);
        myBackend = new MyBackend();
        myBackend.context = AddImageActivity.this;
        if(!myBackend.isUserLogin()){
            myBackend.require = "";
            myBackend.input_email ="";
            startActivity(new Intent(AddImageActivity.this, GraphLoginActivity.class));
            finish();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDashboardFragment();
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch intent to select an image from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement image upload logic here
                if(pickedImage==null){
                    Toast.makeText(AddImageActivity.this,"Please select an Image",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentResolver contentResolver = getContentResolver();
                String fileType = contentResolver.getType(pickedImage).split("/")[1];
                String name = pickedImage.getLastPathSegment()+"."+fileType;

                myBackend.pushUploadImage(pickedImage,name).thenAccept(results ->{
                    if(myBackend.isSucess(results)){
                        Toast.makeText(AddImageActivity.this,myBackend.getMessenge(results),Toast.LENGTH_SHORT).show();
                        navigateToDashboardFragment();
                    }
                    else{
                        Toast.makeText(AddImageActivity.this,myBackend.getMessenge(results),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // Set the selected image to the ImageView
            pickedImage = data.getData();
            imageView.setImageURI(pickedImage);
        }
    }

    private void navigateToDashboardFragment() {
        Intent intent = new Intent(AddImageActivity.this, MainActivity.class);
        intent.putExtra("dashboardFragment", true);
        startActivity(intent);
        finish();
    }
}
