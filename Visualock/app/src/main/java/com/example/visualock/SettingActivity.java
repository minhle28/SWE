package com.example.visualock;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import android.widget.CompoundButton;

public class SettingActivity extends AppCompatActivity {

    private CheckBox checkBoxAgree;
    private Button buttonDeleteAccount;
    private MyBackend myBackend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        myBackend = new MyBackend();
        myBackend.context = SettingActivity.this;
        if(!myBackend.isUserLogin()){
            myBackend.require = "";
            myBackend.input_email ="";
            startActivity(new Intent(SettingActivity.this, GraphLoginActivity.class));
            finish();
        }
        ImageView backButton = findViewById(R.id.backButton);
        checkBoxAgree = findViewById(R.id.checkBoxAgree);
        buttonDeleteAccount = findViewById(R.id.buttonDeleteAccount);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMenuFragment();
            }
        });

        // Add listener to CheckBox
        checkBoxAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Enable/disable button and change its color based on CheckBox state
                if (isChecked) {
                    buttonDeleteAccount.setEnabled(true);
                    buttonDeleteAccount.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    buttonDeleteAccount.setEnabled(false);
                    buttonDeleteAccount.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        });

        buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonDeleteAccount.setEnabled(false);
                myBackend.deleteAccount().thenAccept(results ->{
                    Toast.makeText(SettingActivity.this,myBackend.getMessenge(results),Toast.LENGTH_SHORT).show();
                    if(myBackend.isSucess(results)){
                       startActivity(new Intent(SettingActivity.this, GraphLoginActivity.class));
                       finish();
                   }
                   else{
                       buttonDeleteAccount.setEnabled(true);
                   }
                });
            }
        });
    }
    private void navigateToMenuFragment() {
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        intent.putExtra("menuFragment", true);
        startActivity(intent);
        finish();
    }
}
