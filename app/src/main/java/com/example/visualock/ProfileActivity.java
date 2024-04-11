package com.example.visualock;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.content.Intent;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.View;

import java.util.concurrent.atomic.AtomicBoolean;

public class ProfileActivity extends AppCompatActivity implements EditNameDialogFragment.EditNameDialogListener {

    private TextView tvUserName, tvName, tvEmail;
    private Button editButton, editEmailButton, saveButton;
    private EditText etNewName;
    private MyBackend myBackend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myBackend = new MyBackend();

        tvUserName = findViewById(R.id.tv_user_name);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        editButton = findViewById(R.id.editButton);
        editEmailButton = findViewById(R.id.editEmailButton);
        saveButton = findViewById(R.id.saveButton);
        etNewName = findViewById(R.id.et_new_name);
        refreshInfor();

        ImageView backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMenuFragment();
            }
        });

        editButton.setOnClickListener(v -> {
            showEditNameDialog();
        });

        editEmailButton.setOnClickListener(v -> {
            showEditEmailDialog();
        });

        saveButton.setOnClickListener(v -> {
            String newName = etNewName.getText().toString().trim();
            if (!TextUtils.isEmpty(newName)) {
                //updateUserName(newName);
            } else {
                Toast.makeText(ProfileActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void refreshInfor(){
        myBackend.getDatabase().thenAccept(results ->{
            if(myBackend.isSucess(results)){
                tvUserName.setText(myBackend.userData.getName());
                tvName.setText(myBackend.userData.getName());
                tvEmail.setText(myBackend.getCurrentEmail());
            }
            else{
                startActivity(new Intent(ProfileActivity.this, GraphLoginActivity.class));
                finish();
            }
        });
    }
    private void navigateToMenuFragment() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.putExtra("menuFragment", true);
        startActivity(intent);
        finish();
    }

    private void showEditNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_name, null);
        EditText etNewName = view.findViewById(R.id.et_new_name);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnSave = view.findViewById(R.id.btn_save);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        AtomicBoolean lock = new AtomicBoolean(false);
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            if(lock.get()) return;
            String newName = etNewName.getText().toString().trim();
            if (!TextUtils.isEmpty(newName)) {
                   myBackend.userData.setName(newName);
                   lock.set(true);
                   myBackend.pushDatabase().thenAccept(results->{
                       refreshInfor();
                       dialog.dismiss();
                   });
            } else {
                Toast.makeText(ProfileActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showEditEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_email, null);
        EditText etNewEmail = view.findViewById(R.id.et_new_email);
        EditText etConfirmEmail = view.findViewById(R.id.confirm_new_email); // Add reference to confirm email EditText
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnSave = view.findViewById(R.id.btn_save);
        AtomicBoolean lock = new AtomicBoolean(false);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(v ->{ if(lock.get()) return; dialog.dismiss();});

        btnSave.setOnClickListener(v -> {
            if(lock.get()) return;
            lock.set(true);
            String newEmail = etNewEmail.getText().toString().trim();
            String confirmEmail = etConfirmEmail.getText().toString().trim();
            if (!TextUtils.isEmpty(newEmail) && !TextUtils.isEmpty(confirmEmail)) {
                if (newEmail.equals(confirmEmail)) {

                    // Update email in Firebase Authentication
                    myBackend.changeEmail(newEmail).thenAccept(results->{
                        if(myBackend.isSucess(results)){
                            Toast.makeText(ProfileActivity.this,"Email changed to "+myBackend.getCurrentEmail(), Toast.LENGTH_SHORT).show();
                            refreshInfor();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(ProfileActivity.this, myBackend.getMessenge(results), Toast.LENGTH_SHORT).show();
                            lock.set(false);
                        }
                    });

                } else {
                    Toast.makeText(ProfileActivity.this, "Email do not match", Toast.LENGTH_SHORT).show();
                    lock.set(false);
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Please enter both email fields", Toast.LENGTH_SHORT).show();
                lock.set(false);
            }
        });

        dialog.show();
    }

    @Override
    public void onSaveClicked(String newName) {
        if (!TextUtils.isEmpty(newName)) {
            //updateUserName(newName);
        } else {
            Toast.makeText(ProfileActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }
    }
}
