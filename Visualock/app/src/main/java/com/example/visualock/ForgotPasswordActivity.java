package com.example.visualock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
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
    private Button backButton;
    private MyBackend myBackend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetEmailEditText = findViewById(R.id.reset_email);
        sendResetButton = findViewById(R.id.send_reset_button);
        feedbackTextView = findViewById(R.id.text);
        ImageView backButton = findViewById(R.id.backButton);
        myBackend = new MyBackend();
        if(myBackend.isUserLogin()){
            myBackend.require = "";
            myBackend.input_email = "";
            Toast.makeText(ForgotPasswordActivity.this,"User logined as "+myBackend.getCurrentEmail(),Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
            finish();
        }
        if(!myBackend.require.equals("")){
            resetEmailEditText.setText(myBackend.input_email);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, GraphLoginActivity.class));
            }
        });

        sendResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetButton.setEnabled(false);
                String email = resetEmailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    resetEmailEditText.setError("Email is required");
                    sendResetButton.setEnabled(true);
                    return;
                }
                myBackend.resetPassword(email).thenAccept(results ->{
                    if(myBackend.isSucess(results)){
                        myBackend.require = "Forget";
                        myBackend.input_email = email;
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
                    }
                    else{
                        Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email. ", Toast.LENGTH_SHORT).show();
                        sendResetButton.setEnabled(true);
                    }
                });

            }
        });
    }
}
