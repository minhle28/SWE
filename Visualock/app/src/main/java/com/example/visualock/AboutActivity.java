package com.example.visualock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {
    private MyBackend myBackend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        myBackend =new MyBackend();
        myBackend.context = AboutActivity.this;
        if(!myBackend.isUserLogin()){
            myBackend.require = "";
            myBackend.input_email = "";
            startActivity(new Intent(AboutActivity.this, GraphLoginActivity.class));
            finish();
        }

        ImageView backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMenuFragment();
            }
        });
    }

    private void navigateToMenuFragment() {
        Intent intent = new Intent(AboutActivity.this, MainActivity.class);
        intent.putExtra("menuFragment", true);
        startActivity(intent);
        finish();
    }
}

