package com.example.visualock;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import android.view.View;

public class ProfileActivity extends AppCompatActivity implements EditNameDialogFragment.EditNameDialogListener {

    private TextView tvUserName, tvName, tvEmail;
    private Button editButton, saveButton;
    private EditText etNewName;
    private FirebaseFirestore database;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        tvUserName = findViewById(R.id.tv_user_name);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        editButton = findViewById(R.id.editButton);
        saveButton = findViewById(R.id.saveButton);
        etNewName = findViewById(R.id.et_new_name);

        retrieveUserDetails();

        editButton.setOnClickListener(v -> {
            showEditNameDialog();
        });

        saveButton.setOnClickListener(v -> {
            String newName = etNewName.getText().toString().trim();
            if (!TextUtils.isEmpty(newName)) {
                updateUserName(newName);
            } else {
                Toast.makeText(ProfileActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveUserDetails() {
        if (currentUser != null) {
            String uid = currentUser.getUid();

            database.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String email = currentUser.getEmail();

                    tvUserName.setText(name);
                    tvName.setText(name);
                    tvEmail.setText(email);
                }
            }).addOnFailureListener(e -> {
                // Handle failure
            });
        }
    }

    private void showEditNameDialog() {
        DialogFragment dialog = new EditNameDialogFragment();
        dialog.show(getSupportFragmentManager(), "EditNameDialogFragment");
    }

    private void updateUserName(String newName) {
        if (currentUser != null) {
            String uid = currentUser.getUid();

            database.collection("users").document(uid).update("name", newName)
                    .addOnSuccessListener(aVoid -> {
                        tvUserName.setText(newName);
                        tvName.setText(newName);
                        etNewName.setVisibility(View.GONE);
                        saveButton.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProfileActivity.this, "Failed to update name", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public void onSaveClicked(String newName) {
        if (!TextUtils.isEmpty(newName)) {
            updateUserName(newName);
        } else {
            Toast.makeText(ProfileActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }
    }
}
