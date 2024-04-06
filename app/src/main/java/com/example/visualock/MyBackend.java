package com.example.visualock;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.lang.String;

public class MyBackend {
    private FirebaseAuth auth;
    private String root_email= "root@gmail.com";
    private String root_pass= "showmethemoney";
    public static User userData;
    private FirebaseFirestore firestore;
    public MyBackend()
    {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        root_Login();
    }
    private void root_Login(){
        auth.signInWithEmailAndPassword(root_email , root_pass);
    }
    private void logOut(){
        auth.signOut();
    }

    // THIS IS UPDATE DB AS CURRENT USER DATA
    private CompletableFuture<Boolean> updateDatabase(){
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        FirebaseUser user = auth.getCurrentUser();
        if(user !=null) {
            DocumentReference userRef = firestore.collection("users").document(user.getEmail());
            // update Database base on currentUser data.
            userRef.set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    future.complete(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    future.complete(false);
                }
            });
        }
        else{
            future.complete(false);
        }
        return future;
    }
    //THIS IS UPDATE DB BY NEW DATA (for Register only, may be)
    private CompletableFuture<Boolean> setDatabase(User newUserData){
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        userData=newUserData;
        updateDatabase().thenAccept(result ->{
            future.complete(result);
        });
        return future;
    }
    // THIS IS GET DATA BY EMAIL
    private CompletableFuture<Boolean> getDatabase(String email){
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            root_Login();
        }
        DocumentReference userRef = firestore.collection("users").document(email);
        // get database
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // DocumentSnapshot exists, map it to your class
                        User user = document.toObject(User.class);
                        if (user != null) {
                            userData = user;
                            future.complete(true);
                        }
                    } else {
                        // DocumentSnapshot doesn't exist
                        userData = null;
                        future.complete(true);
                    }
                } else {
                    // An error occurred while retrieving the document
                    //Log.d(TAG, "Error getting document", task.getException());
                    future.complete(false);
                }
            }
        });

        return future;
    }
    // THIS IS USER LOGIN BY IMAGE SELECTED
    public CompletableFuture<String> logIn(String email, List<String> image64 ){
        CompletableFuture<String> future = new CompletableFuture<>();
        getDatabase(email).thenAccept(results ->{
            if(isSucess(results.toString())){
                // get data OK
                if(userData != null)
                // Check pass choiced
                for (String image:image64) {
                    if(!userData.getImages_pass().contains(image)){
                        future.complete("false:Wrong Password");
                        return;
                    }
                }
                // logOut all
                logOut();
                // get password
                String password = generation_Pass();
                // user get in
                auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> {
                            // Get the signed-in user
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                if (userData == null) {
                                    // database error, let recovery it
                                    User newUser = new User(email.split("@")[0]);
                                    userData=newUser;
                                    updateDatabase();
                                 }
                                future.complete("true:Sign success");
                            }
                            else {
                                root_Login();
                                future.complete("false:Wrong Email or Password");
                            }
                        })
                        .addOnFailureListener(e -> future.complete("false:Log in Exception "+e.getMessage().replace(":","_")));
            }
        });
        return future;
    }
    // THIS IS USER LOGIN BY PASSWORD
    public CompletableFuture<String> logIn(String email, String password){
        CompletableFuture<String> future = new CompletableFuture<>();

        // logOut all
        logOut();
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // Login success
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        getDatabase(email).thenAccept(results ->{
                            if(isSucess(results.toString())) {
                                // get data OK
                                if (userData != null){
                                    if(userData.getImages_pass().size() > 0 ){
                                        String oldPassword = generation_Pass();
                                        auth.getCurrentUser().updatePassword(oldPassword);
                                    }
                                }
                                // user get in
                                if (userData == null) {
                                    // database error, let recovery it
                                    User newUser = new User(email.split("@")[0]);
                                    userData=newUser;
                                    updateDatabase();
                                }
                                future.complete("true:Sign success");
                            }
                        });
                    }
                    else {
                        root_Login();
                        future.complete("false:Wrong Email or Password");
                    }
                })
                .addOnFailureListener(e -> future.complete("false:Log in Exception "+e.getMessage().replace(":","_")));
        return future;
    }
    // THIS IS CHANGE PASSWORD WITH TEXT INPUT, NO IMAGE
    public CompletableFuture<String> changePassword(String newPassword){
        CompletableFuture<String> future = new CompletableFuture<>();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && !isRoot()) {
            // new password to Authentication
            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // Move img pass to normal if avaible
                    for(String image: userData.getImages_pass()) {
                        if(!userData.getImages().contains(image)) {
                            userData.insertImages(image);
                            userData.removeImages_pass(image);
                        }
                    }
                    // update database
                    updateDatabase().thenAccept(result ->{
                        if(result){
                            future.complete("true:Change Password Successful");
                        }else{
                            future.complete("false:Change Password Fail");
                        }

                    });
                }
            });
        }
        return future;
    }

    // THIS IS CHANGE PASSWORD BASE ON IMAGE IN CURRENT USER DATA
    public CompletableFuture<String> changePassword(){
        CompletableFuture<String> future = new CompletableFuture<>();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && !isRoot()) {
            //generation pass
            String newPassword = generation_Pass();
            // new password to Authentication
            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // update database
                    updateDatabase().thenAccept(result ->{
                        if(result){
                            future.complete("true:Change Password Successful");
                        }else{
                            future.complete("false:Change Password Fail");
                        }
                    });
                }
            });
        }
        return future;
    }

    private String getHashPass(List<String> image64){
        return "";
    }
    private String generation_Pass() {
        String[] parameters = userData.getParameter().split(":");
        String pass ="";
        int i =0;
        for (String image: userData.getImages_pass()
             ) {
            pass += image.substring(image.length()/2,image.length()/2+ Integer.parseInt(parameters[i]));
            i++;
        }
        return pass;
    }

    public List<String> get_25picture() {
        // mission return 25 picture

        return null;
    }

    private boolean isRoot(){
        if(auth.getCurrentUser() == null ) return false;
        return auth.getCurrentUser().getEmail().compareTo(root_email)==0;
    }
    public boolean isSucess(String result){
        return result.toString().split(":")[0] =="true";
    }

}
