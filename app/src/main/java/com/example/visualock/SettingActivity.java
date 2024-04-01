package com.example.visualock;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import android.widget.CompoundButton;

public class SettingActivity extends AppCompatActivity {

    private CheckBox checkBoxAgree;
    private Button buttonDeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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
    }

    private void navigateToMenuFragment() {
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        intent.putExtra("menuFragment", true);
        startActivity(intent);
        finish();
    }
}
