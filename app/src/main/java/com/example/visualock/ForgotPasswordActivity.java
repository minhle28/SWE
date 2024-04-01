package com.example.visualock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText resetEmailEditText;
    private Button sendResetButton;
    private TextView feedbackTextView;
    private FirebaseAuth auth;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetEmailEditText = findViewById(R.id.reset_email);
        sendResetButton = findViewById(R.id.send_reset_button);
        feedbackTextView = findViewById(R.id.text);
        ImageView backButton = findViewById(R.id.backButton);
        auth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, GraphLoginActivity.class));
            }
        });

        sendResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = resetEmailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    resetEmailEditText.setError("Email is required");
                    return;
                }

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Sent", Toast.LENGTH_SHORT).show();

                                    feedbackTextView.setVisibility(View.VISIBLE);
                                    feedbackTextView.setText("Please check your email for password reset link");

                                    // Delayed redirection to login activity after 3 seconds
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    }, 3000);
                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
