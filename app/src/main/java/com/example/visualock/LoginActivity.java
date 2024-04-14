package com.example.visualock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail, loginPassword;
    private TextView registerRedirectText;
    private TextView loginByGraph;
    private TextView forgot_passRedirectText;
    private Button loginButton;

    private MyBackend myBackend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if the user is already authenticated


        myBackend = new MyBackend();
        if (myBackend.isUserLogin()) {
            Toast.makeText(LoginActivity.this,"User logined as "+myBackend.getCurrentEmail(),Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        loginByGraph = findViewById(R.id.loginByGraph);
        if(!myBackend.require.equals("")){
            loginEmail.setText(myBackend.input_email);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                String pass = loginPassword.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        // login by text pass
                        myBackend.logIn(email,pass).thenAccept(results -> {
                            if(myBackend.isSucess(results)){
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        loginPassword.setError("Password cannot be empty");
                    }
                } else if (email.isEmpty()) {
                    loginEmail.setError("Email cannot be empty");
                } else {
                    loginEmail.setError("Please enter valid email");
                }
            }
        });

        loginByGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, GraphLoginActivity.class));
            }
        });
    }
}
