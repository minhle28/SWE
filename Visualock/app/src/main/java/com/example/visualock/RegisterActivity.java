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

public class RegisterActivity extends AppCompatActivity {

    private EditText registerEmail, registerPassword, registerName;
    private Button registerButton;
    private TextView loginRedirectText;
    private MyBackend myBackend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myBackend = new MyBackend();
        registerEmail = findViewById(R.id.register_email);
        registerName = findViewById(R.id.register_name);
        registerButton = findViewById(R.id.register_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);


        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, GraphLoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = registerEmail.getText().toString();
                String name = registerName.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    myBackend.is_Email_Registered(email).thenAccept(results ->{
                        if(!myBackend.isSucess(results)){
                            myBackend.require = "Register";
                            myBackend.input_name= name;
                            myBackend.input_email = email;
                            startActivity(new Intent(RegisterActivity.this, GraphPasswordActivity.class));
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Re-check Email Format", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

}
