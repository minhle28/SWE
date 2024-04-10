package com.example.visualock;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.lang.String;
import java.util.concurrent.atomic.AtomicInteger;

public class MyBackend {
    private FirebaseAuth auth;
    private String root_email= "root@gmail.com";
    private String root_pass= "showmethemoney";
    public static User userData;
    public static String require="";
    public static String input_email="";
    public static String input_name="";
    public List<String> defaultImages;
    public List<String> userUploadImages;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage storage;
    public Context context;
    public MyBackend()
    {
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        defaultImages = new ArrayList<>();
        //root_Login();
    }
    private void root_Login(){
        auth.signInWithEmailAndPassword(root_email , root_pass);
    }
    private void logOut(){
        auth.signOut();
    }

    // THIS IS UPDATE DB AS CURRENT USER DATA
    public CompletableFuture<Boolean> pushDatabase(){
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        FirebaseUser user = auth.getCurrentUser();
        if(isUserLogin()) {
            DocumentReference userRef = firebaseFirestore.collection("users").document(user.getEmail());
            // update Database base on currentUser data.
            userRef.set(userData).addOnCompleteListener(task -> future.complete(true))
                    .addOnFailureListener(e -> future.complete(false));
        }
        else{
            future.complete(false);
        }
        return future;
    }
    public CompletableFuture<String> getDatabase(){
        CompletableFuture<String> future = new CompletableFuture<>();
        if(!isUserLogin()){
            future.complete("false:Did not login");
            return future;
        }
        return getDatabase(auth.getCurrentUser().getEmail());
    }
    // THIS IS GET DATA BY EMAIL
    public CompletableFuture<String> getDatabase(String email){
        CompletableFuture<String> future = new CompletableFuture<>();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            root_Login();
        }
        DocumentReference userRef = firebaseFirestore.collection("users").document(email);
        userData = null; //empty it
        // get database
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // DocumentSnapshot exists, map it to your class
                    User user1 = document.toObject(User.class);
                    if (user1 != null) {
                        userData = user1;
                        future.complete("true: Get UserData success "+userData.getImages_pass().size() );
                    }
                } else {
                    // DocumentSnapshot doesn't exist
                    userData = null;
                    future.complete("true: UserData empty");
                }
            } else {
                // An error occurred while retrieving the document
                //Log.d(TAG, "Error getting document", task.getException());
                future.complete("false: Get UserData error");
            }
            if(!isUserLogin())
                logOut();
        });

        return future;
    }
    // THIS IS USER LOGIN BY IMAGE SELECTED
    public CompletableFuture<String> logIn(String email, List<String> clickedImage ){
        CompletableFuture<String> future = new CompletableFuture<>();
        getDatabase(email).thenAccept(results ->{
            if(isSucess(results)){
                // get data OK
                if(userData != null) {
                    // Check pass choiced
                    for (String image : clickedImage) {
                        if (!userData.getImages_pass().contains(image)) {
                            future.complete("false:Wrong Password");
                            return;
                        }
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
                            if (isUserLogin()) {
                                userData =null;
                                getDatabase();
                                future.complete("true:Sign success");
                            }
                            else {
                                future.complete("false:Wrong Email or Password");
                            }
                        })
                        .addOnFailureListener(e -> future.complete("false:Log in Exception "+e.getMessage()));
            }
        });
        return future;
    }
    // THIS IS USER LOGIN BY PASSWORD (For FORGET PASSWORD)
    public CompletableFuture<String> logIn(String email, String password){
        CompletableFuture<String> future = new CompletableFuture<>();

        // logOut all
        logOut();
        userData=null;
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // Login success
                    if (isUserLogin()) {
                        getDatabase().thenAccept(results ->{
                            if(isSucess(results)) {
                                // get data OK
                                if (userData != null){
                                    if(userData.getImages_pass().isEmpty()){
                                        String oldPassword = generation_Pass();
                                        auth.getCurrentUser().updatePassword(oldPassword);
                                    }
                                }
                                // user get in
                                if (userData == null) {
                                    // database error, let recovery it
                                    userData= new User(auth.getUid(), email.split("@")[0]);
                                    pushDatabase();
                                }
                                future.complete("true:Sign success");
                            }
                        });
                    }
                    else {
                        future.complete("false:Wrong Email or Password");
                    }
                })
                .addOnFailureListener(e -> future.complete("false:Log in Exception "+e.getMessage()));
        return future;
    }

    //THIS REGISTER FOR THE FIRST TIME
    public CompletableFuture<String> signUp(String email,List<String> clickedImage){
        CompletableFuture<String> future = new CompletableFuture<>();
        // Generation parameters to make password unpredictable

        String password =generation_Pass(clickedImage);
        // logOut all
        logOut();
        auth.createUserWithEmailAndPassword(email, password.split(";")[1])
                .addOnSuccessListener(authResult -> {
                    // Login success
                    //Toast.makeText(context,"Account created...",Toast.LENGTH_SHORT).show();
                    if (isUserLogin()) {
                        // put user
                        userData= new User(auth.getCurrentUser().getUid(),email.split("@")[0],password.split(";")[0],clickedImage);
                       // Toast.makeText(context,"Saving passcode...",Toast.LENGTH_SHORT).show();
                        pushDatabase().thenAccept(result->{
                            Toast.makeText(context,"Saving done...",Toast.LENGTH_SHORT).show();
                            if(result){
                                future.complete("true:Register Successful");
                            }
                            else{
                                future.complete("false:Register Fail");
                            }
                        });
                    }
                    else {
                        future.complete("false:Register Fail");
                    }
                })
                .addOnFailureListener(e -> future.complete("false:Log in Exception "+e.getMessage()));
        return future;
    }

    // THIS IS CHANGE PASSWORD BASE ON IMAGE IN CURRENT USER DATA
    public CompletableFuture<String> changePassword(){
        return changePassword(generation_Pass());
    }
    // THIS IS CHANGE PASSWORD WITH TEXT INPUT, NO IMAGE
    public CompletableFuture<String> changePassword(String newPassword){
        CompletableFuture<String> future = new CompletableFuture<>();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && !isRoot()) {
            // new password to Authentication
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                // update database
                pushDatabase().thenAccept(result ->{
                    if(result){
                        future.complete("true:Change Password Successful");
                    }else{
                        future.complete("false:Change Password Fail");
                    }
                });
            });
        }
        return future;
    }

    // THIS IS CHANGE THE EMAIL
    public CompletableFuture<String> changeEmail(String newEmail){
        CompletableFuture<String> future = new CompletableFuture<>();
        if (isUserLogin()) {
            String oldEmail = auth.getCurrentUser().getEmail();
            // Querry 1
            is_Email_Registered(newEmail).thenAccept(result1->{
                if(isSucess(result1)) {
                    // Querry 2
                    getDatabase().thenAccept(result2->{
                        if(isSucess(result2)) {
                            auth.getCurrentUser().updateEmail(newEmail).addOnCompleteListener(task -> {
                                // Querry 3
                                // change database
                                pushDatabase().thenAccept(result3 -> {
                                    if(result3) {
                                        firebaseFirestore.collection("users").document(oldEmail).delete();
                                        // change storage (not need, file upload by uID)
                                        future.complete("true:Change Email Successful");
                                    }
                                    else{
                                        future.complete("false:Change Email Fail(3)");
                                    }
                                });
                            });
                        }
                        else{
                            future.complete("false:Change Email Fail(2)");
                        }
                    });
                }
                else{
                    future.complete("false:New Email is Existed");
                }
            });
        }
        return future;
    }

    // THIS SEND EMAIL TO RESET PASSWORD
    public CompletableFuture<String> resetPassword(String email){
        CompletableFuture<String> future = new CompletableFuture<>();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            future.complete("true:Sent reset email.");
                        } else {
                            future.complete("false:Error in send reset email.");
                        }
                    }
                });
        return future;
    }

    /////////////////////////////////////////////////////////////////////// Images Handle ////////////////////////
    // THIS GET Default PICTURE FOR LISTVIEW (PASS TEST)
    public CompletableFuture<String> getDefaultImages(){
        CompletableFuture<String> future = new CompletableFuture<>();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            root_Login();
        }
        StorageReference folderRef = storage.getReference().child("default");
        folderRef.listAll().addOnSuccessListener(listResult -> {
            List<String> listURL = new ArrayList<>();
            int totalItems = listResult.getItems().size();
            AtomicInteger count = new AtomicInteger(0);

            for (StorageReference item : listResult.getItems()) {
                // Get the download URL for each file
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Handle the download URL
                    listURL.add(uri.toString());
                    count.incrementAndGet();
                    if (count.get() == totalItems) {
                        // All URLs retrieved, do something with listURL
                        defaultImages = listURL;
                        future.complete("true:Loaded default image "+defaultImages.size());
                        Toast.makeText(context,"Loaded default image"+defaultImages.size(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if(!isUserLogin())
                logOut();
        });
        return future;
    }
    // THIS for UPLOAD PICTURE
    public CompletableFuture<String> pushUploadImage(Uri uri){
        CompletableFuture<String> future = new CompletableFuture<>();
        FirebaseUser user = auth.getCurrentUser();
        if (!isUserLogin()) {
           future.complete("false:Did not login");
           return future;
        }
        StorageReference folderRef = storage.getReference().child(auth.getUid());
        folderRef.putFile(uri).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                getUploadImages(auth.getUid()).thenAccept(results -> {
                    future.complete("true:Uploaded image "+userUploadImages.size());
                    //Toast.makeText(context,"Loaded User Upload image"+userUploadImages.size(),Toast.LENGTH_SHORT).show();
                });
            }
            else{
                future.complete("false:Upload Fail");
            }
        });
        return future;
    }

    // THIS GET Upload PICTURE FOR LISTVIEW (PASS TEST)
    public CompletableFuture<String> getUploadImages(String uID){
        CompletableFuture<String> future = new CompletableFuture<>();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            root_Login();
        }
        StorageReference folderRef = storage.getReference().child(uID);
        folderRef.listAll().addOnSuccessListener(listResult -> {
            List<String> listURL = new ArrayList<>();
            int totalItems = listResult.getItems().size();
            AtomicInteger count = new AtomicInteger(0);

            for (StorageReference item : listResult.getItems()) {
                // Get the download URL for each file
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Handle the download URL
                    listURL.add(uri.toString());
                    count.incrementAndGet();
                    if (count.get() == totalItems) {
                        // All URLs retrieved, do something with listURL
                        userUploadImages = listURL;
                        future.complete("true:Loaded User Upload image "+userUploadImages.size());
                        Toast.makeText(context,"Loaded User Upload image"+userUploadImages.size(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if(!isUserLogin())
                logOut();
        });
        return future;
    }

    // THIS IS CHECK EMAIL REGISTED OR NOT
    public CompletableFuture<String> is_Email_Registered(String email){
        CompletableFuture<String> future = new CompletableFuture<>();
        //future.complete("true:Email is registered");
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> signInMethods = task.getResult().getSignInMethods();
                        System.out.println("Sign method = "+signInMethods.size());
                        if (signInMethods != null && !signInMethods.isEmpty()) {
                            // Email is registered
                            future.complete("true:Email is registered");
                        } else {
                            // Email is not registered
                            future.complete("false:Email is not registered");
                        }
                    } else {
                        // Error occurred while checking email registration
                        future.complete("false:Email check error");
                    }
                });

        return future;
    }
    public String getCurrentEmail(){
        if(auth.getCurrentUser()==null) return "";
        return auth.getCurrentUser().getEmail();
    }
    private String generation_Pass() {
        return generation_Pass(userData.getImages_pass());
    }
    private String generation_Pass(List<String> images) {
        StringBuilder pass = new StringBuilder();
        Random random = new Random();
        int[] parameter_int = new int[6];
        for(int i =0 ;i<5; i++){
            parameter_int[i] = random.nextInt(3)+8;
        }
        // saving parameters as xx:xx:xx:xx
        String parameter_String= String.join(":", Arrays.stream(parameter_int)
                .mapToObj(String::valueOf)
                .toArray(String[]::new));
        int j =0;
        for (String image: images
        ) {
            String target = image.substring(image.indexOf("token="));
            pass.append(target.substring(target.length() / 2, target.length() / 2 + parameter_int[j]));
            j++;
        }
        return parameter_String+";"+pass;
    }

    private boolean isRoot(){
        if(auth.getCurrentUser() == null ) return false;
        return auth.getCurrentUser().getEmail().equals(root_email);
    }
    public boolean isUserLogin(){
        if(auth.getCurrentUser() == null ) return false;
        return !auth.getCurrentUser().getEmail().equals(root_email);
    }
    public boolean isSucess(String result){
        return result.split(":")[0].equals("true");
    }
    public String getMessenge(String result){
        String output = "";
        String[] tmp = result.split(":");
        for(int i=1; i <tmp.length;i++){
            output+=tmp[i];
        }
        return output;
    }

}
