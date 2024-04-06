package com.example.visualock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText registerEmail, registerPassword, registerName;
    private Button registerButton;
    private TextView loginRedirectText;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Firebase Authentication initialization
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        registerEmail = findViewById(R.id.register_email);
        registerName = findViewById(R.id.register_name);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        /*
        registerPassword = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = registerName.getText().toString().trim();
                String user = registerEmail.getText().toString().trim();
                String pass = registerPassword.getText().toString().trim();

                if (!isValidEmail(user)) {
                    registerEmail.setError("Invalid email format");
                    return;
                }

                User user1 = new User(name, user, pass);

                if (pass.isEmpty()) {
                    registerPassword.setError("Password cannot be empty");
                    return;
                }

                // Create user with email and password
                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                currentUser.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Verification email sent. Please verify your email address.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }

                            String uid = task.getResult().getUser().getUid();
                            database
                                    .collection("users")
                                    .document(uid)
                                    .set(user1)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Failed to set display name", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            firebaseUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Failed to set display name", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
        */
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, GraphLoginActivity.class));
            }
        });
    }

}
