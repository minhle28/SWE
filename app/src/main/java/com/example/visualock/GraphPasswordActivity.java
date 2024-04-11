package com.example.visualock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;
import android.widget.GridView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GraphPasswordActivity extends AppCompatActivity {

    private static final int MAX_CLICKS = 5; // Maximum allowed clicks
    private static List<String> clickedImage;
    private boolean isLoading =true;
    private MyBackend myBackend;
    public class PasswordImageAdapter extends BaseAdapter {
        // Other methods and fields

        private Context mContext;
        private List<String> myListImages;

        public PasswordImageAdapter(Context context, List<String> myListImages) {
            mContext = context;
            this.myListImages = myListImages;
        }

        @Override
        public int getCount() {
            return myListImages.size();
        }

        @Override
        public Object getItem(int position) {
            return myListImages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            String chosenURL = myListImages.get(position);
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.get().load(chosenURL).into(imageView);
            } else {
                imageView = (ImageView) convertView;
            }


            // Apply transparent grey overlay if the image is clicked
            if (clickedImage.contains(chosenURL)) {
                imageView.setColorFilter(Color.parseColor("#80000000"));
            } else {
                imageView.setColorFilter(null);
            }

            // Toggle clicked state on click
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isLoading) return;
                    // Check if the maximum number of clicks is reached
                    if (clickedImage.contains(chosenURL)) {
                        // clicked, now remove click
                        imageView.setColorFilter(null);
                        clickedImage.remove(chosenURL);
                    } else {
                        // add click
                        if(clickedImage.size()<MAX_CLICKS){
                            clickedImage.add(chosenURL);
                            imageView.setColorFilter(Color.parseColor("#80000000"));
                        }
                        else{
                            Toast.makeText(mContext, "You can only select up to 5 images.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    notifyDataSetChanged(); // Refresh the adapter to update the UI
                }
            });
            return imageView;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_password);
        TextView topLabel = findViewById(R.id.topLabel);
        topLabel.setText("Loading Picture");

        clickedImage = new ArrayList<>();
        myBackend = new MyBackend();
        myBackend.context= GraphPasswordActivity.this;

        List<String> selectedImages = new ArrayList<>();
        ListView[] rowListView = new ListView[6];
        rowListView[0] = findViewById(R.id.row1);
        rowListView[1] = findViewById(R.id.row2);
        rowListView[2] = findViewById(R.id.row3);
        rowListView[3] = findViewById(R.id.row4);
        rowListView[4] = findViewById(R.id.row5);
        rowListView[5] = findViewById(R.id.row6);
        // Loading picture to ListView
        switch (myBackend.require){
            case "Register":
                myBackend.getDefaultImages().thenAccept(results ->{
                    if(myBackend.isSucess(results)){
                        for (String image:myBackend.defaultImages
                        ) {
                            selectedImages.add(image);
                        }
                        // shufft
                        //Collections.shuffle(selectedImages);
                        int n1 = selectedImages.size()/6;
                        Toast.makeText(GraphPasswordActivity.this,myBackend.getMessenge(results), Toast.LENGTH_SHORT).show();
                        // div images into 6 row
                        for(int i=0; i<6; i++){
                            rowListView[i].setAdapter(new PasswordImageAdapter(this, selectedImages.subList(n1*i,n1*(i+1))));
                        }
                        topLabel.setText("Pick Picture");
                        isLoading=false;
                    }
                });
                break;
            case "Login":
                // 36 images limit
                //Query 1
                myBackend.getDatabase(myBackend.input_email).thenAccept(results1 ->{
                    if(myBackend.isSucess(results1)){
                        //Query 2
                        Toast.makeText(GraphPasswordActivity.this, myBackend.getMessenge(results1), Toast.LENGTH_SHORT).show();
                        myBackend.getUploadImages(myBackend.userData.getuID()).thenAccept(results2->{
                            //Query 3
                            Toast.makeText(GraphPasswordActivity.this, myBackend.getMessenge(results2), Toast.LENGTH_SHORT).show();
                            myBackend.getDefaultImages().thenAccept(results3 ->{
                                try {
                                    int n2 = 36;
                                    Toast.makeText(GraphPasswordActivity.this, myBackend.getMessenge(results3), Toast.LENGTH_SHORT).show();
                                    if(myBackend.userData.getImages_pass()!=null)
                                        for (String image : myBackend.userData.getImages_pass()
                                        ) {
                                            if (!selectedImages.contains(image)) {
                                                selectedImages.add(image);
                                                n2--;
                                            }
                                        }
                                    if (myBackend.isSucess(results2)) {
                                        if (myBackend.userUploadImages != null)
                                            for (String image : myBackend.userUploadImages
                                            ) {
                                                if (!selectedImages.contains(image)) {
                                                    selectedImages.add(image);
                                                    n2--;
                                                    if (n2 <= 0) break;
                                                }
                                            }
                                    }
                                    if (myBackend.isSucess(results3)) {
                                        if (n2 > 0) {
                                            if(myBackend.defaultImages!=null)
                                                for (String image : myBackend.defaultImages
                                                ) {
                                                    if (!selectedImages.contains(image)) {
                                                        selectedImages.add(image);
                                                        n2--;
                                                        if (n2 <= 0) break;
                                                    }
                                                }
                                        }
                                    }
                                    System.out.println(selectedImages.get(5));
                                    //Collections.shuffle(selectedImages);
                                    for(int i=0; i<6; i++){
                                        rowListView[i].setAdapter(new PasswordImageAdapter(this, selectedImages.subList(6*i,6*(i+1))));
                                    }
                                }
                                catch (Exception ex){
                                    Toast.makeText(GraphPasswordActivity.this, "Error:"+ex.getMessage(), Toast.LENGTH_SHORT).show();
                                    System.out.println(ex.getMessage());
                                }
                                // shufft ?
                                topLabel.setText("Pick Picture");
                                isLoading=false;
                            });
                        });
                    }
                    else{
                        Toast.makeText(GraphPasswordActivity.this, "Error Connect", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

        ImageView backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GraphPasswordActivity.this, GraphLoginActivity.class));
            }
        });

        Button login_button = findViewById(R.id.login_button);
        login_button.setText(myBackend.require);
        // Action for button
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoading) return;
                topLabel.setText("Encoding Password...");
                isLoading=true;
                switch (myBackend.require) {
                    case "Register":
                        myBackend.signUp(myBackend.input_email,myBackend.input_name,clickedImage).thenAccept(results ->{
                            Toast.makeText(GraphPasswordActivity.this, myBackend.getMessenge(results), Toast.LENGTH_SHORT).show();
                            if (myBackend.isSucess(results)) {
                                startActivity(new Intent(GraphPasswordActivity.this, MainActivity.class));
                                finish();
                            }else{
                                topLabel.setText("Pick Image");
                                isLoading=false;
                            }
                        });
                        break;
                    case "Login":
                        myBackend.logIn(myBackend.input_email,clickedImage).thenAccept(results -> {
                            Toast.makeText(GraphPasswordActivity.this, myBackend.getMessenge(results), Toast.LENGTH_SHORT).show();
                            if (myBackend.isSucess(results)) {
                                startActivity(new Intent(GraphPasswordActivity.this, MainActivity.class));
                                finish();
                            } else {
                                topLabel.setText("Pick Image");
                                isLoading=false;
                            }
                        });
                        break;
                }
            }
        });

    }
}
