package com.example.visualock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        myBackend.context = GraphLoginActivity.this;
        // Check if the user is already authenticated
        if (myBackend.isUserLogin()) {
            myBackend.require = "";
            myBackend.input_email = "";
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
                String email = loginEmail.getText().toString();
                myBackend.require = "Transfer";
                myBackend.input_email = email;
                startActivity(new Intent(GraphLoginActivity.this, LoginActivity.class));
            }
        });

        forgot_passRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                myBackend.require = "Transfer";
                myBackend.input_email = email;
                startActivity(new Intent(GraphLoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        password_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    System.out.println(email);
                    myBackend.is_Email_Registered(email).thenAccept(results ->{
                        if(myBackend.isSucess(results)){
                            myBackend.require = "Login";
                            myBackend.input_email = email;
                            startActivity(new Intent(GraphLoginActivity.this, GraphPasswordActivity.class));
                        }
                        else{
                            Toast.makeText(GraphLoginActivity.this, myBackend.getMessenge(results), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(GraphLoginActivity.this, "Re-check Email Format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
