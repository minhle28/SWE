package com.example.visualock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class GraphLoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private TextView registerRedirectText;
    private TextView loginByTextual;
    private TextView forgot_passRedirectText;
    private Button password_redirect;
    private MyBackend myBackend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_login);
        myBackend = new MyBackend();
        // Check if the user is already authenticated
        if (myBackend.isUserLogin()) {
            startActivity(new Intent(GraphLoginActivity.this, MainActivity.class));
            finish();
        }

        loginEmail = findViewById(R.id.login_email);

        registerRedirectText = findViewById(R.id.registerRedirectText);
        loginByTextual = findViewById(R.id.loginByTextual);
        forgot_passRedirectText = findViewById(R.id.forgot_passRedirectText);
        password_redirect = findViewById(R.id.password_button);

        registerRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GraphLoginActivity.this, RegisterActivity.class));
            }
        });

        loginByTextual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GraphLoginActivity.this, LoginActivity.class));
            }
        });

        forgot_passRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GraphLoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        password_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GraphLoginActivity.this, GraphPasswordActivity.class));
            }
        });
    }
}
