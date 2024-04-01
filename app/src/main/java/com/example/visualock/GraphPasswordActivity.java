package com.example.visualock;
import java.util.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.GridView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.AdapterView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class GraphPasswordActivity extends AppCompatActivity {
    public static class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private int[] mImageIds;

        public ImageAdapter(Context context, int[] imageIds) {
            mContext = context;
            mImageIds = imageIds;
        }

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public Object getItem(int position) {
            return mImageIds[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // If convertView is null, inflate a new ImageView
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                // If convertView is not null, reuse it
                imageView = (ImageView) convertView;
            }

            // Set the image resource for the ImageView
            imageView.setImageResource(mImageIds[position]);
            return imageView;
        }

    }

    int[] colorImages = {
            R.drawable.grey, R.drawable.pink, R.drawable.green, R.drawable.orange, R.drawable.yellow,
            R.drawable.blue, R.drawable.black, R.drawable.red, R.drawable.purple
    };

    int[] treeImages = {
            R.drawable.birch, R.drawable.cedar, R.drawable.maple, R.drawable.elm, R.drawable.willow, R.drawable.pine,
            R.drawable.oak
    };

    int[] dailyObjectsImages = {
            R.drawable.phone, R.drawable.carrot, R.drawable.mouse, R.drawable.balloon, R.drawable.boat,
            R.drawable.glasses, R.drawable.table, R.drawable.keyboard, R.drawable.sun, R.drawable.book,
            R.drawable.cloud, R.drawable.bottle, R.drawable.leaf, R.drawable.apple, R.drawable.plant,
            R.drawable.key, R.drawable.mug, R.drawable.shoe
    };

    int[] animalImages = {
            R.drawable.bear, R.drawable.rabbit, R.drawable.turtle, R.drawable.bird, R.drawable.giraffe, R.drawable.snake,
            R.drawable.monkey, R.drawable.zebra, R.drawable.cat, R.drawable.koifish, R.drawable.fish, R.drawable.lion,
            R.drawable.dog, R.drawable.squirrel, R.drawable.polar_bear, R.drawable.kangroo, R.drawable.frog, R.drawable.tiger
    };

    int[] placesImages = {
            R.drawable.leaningtowerofpisa, R.drawable.colosseum, R.drawable.greatwallofchina, R.drawable.pyramidsofgiza,
            R.drawable.christtheredeemer, R.drawable.neuschwansteincastle, R.drawable.stonehenge, R.drawable.acropolisofathens,
            R.drawable.petronastowers, R.drawable.tajmahal, R.drawable.operahouse, R.drawable.machupicchu, R.drawable.eiffeltower,
            R.drawable.goldengate, R.drawable.burjkhalifa
    };

    int[] vehicleImages = {
            R.drawable.bike, R.drawable.firetruck, R.drawable.truck, R.drawable.sailboat, R.drawable.sportscar,
            R.drawable.pickuptruck, R.drawable.cycle, R.drawable.cycle2, R.drawable.jetski, R.drawable.bus,
            R.drawable.cargoship, R.drawable.helicopter, R.drawable.plane, R.drawable.mountrushmore, R.drawable.scooter,
            R.drawable.ambullance, R.drawable.metro
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_password);

        ListView Tree = findViewById(R.id.cat_tree);
        Tree.setAdapter(new ImageAdapter(this, treeImages));
        ListView Color = findViewById(R.id.cat_color);
        Color.setAdapter(new ImageAdapter(this, colorImages));
        ListView dailyObjects = findViewById(R.id.cat_dailyObjects);
        dailyObjects.setAdapter(new ImageAdapter(this,dailyObjectsImages));
        ListView Animals = findViewById(R.id.cat_animals);
        Animals.setAdapter(new ImageAdapter(this, animalImages));
        ListView Places = findViewById(R.id.cat_places);
        Places.setAdapter(new ImageAdapter(this, placesImages));
        ListView Vehicle = findViewById(R.id.cat_vehicles);
        Vehicle.setAdapter(new ImageAdapter(this,vehicleImages));

        Tree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GraphPasswordActivity.this, "Tree image clicked at position: " + position, Toast.LENGTH_SHORT).show();


            }
        });

        Color.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GraphPasswordActivity.this, "Color image clicked at position: " + position, Toast.LENGTH_SHORT).show();


            }
        });

        dailyObjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GraphPasswordActivity.this, "Daiily Object image clicked at position: " + position, Toast.LENGTH_SHORT).show();


            }
        });

        Animals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GraphPasswordActivity.this, "Animals image clicked at position: " + position, Toast.LENGTH_SHORT).show();


            }
        });

        Places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GraphPasswordActivity.this, "Places image clicked at position: " + position, Toast.LENGTH_SHORT).show();


            }
        });

        Vehicle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GraphPasswordActivity.this, "Vehicle image clicked at position: " + position, Toast.LENGTH_SHORT).show();


            }
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GraphPasswordActivity.this, GraphLoginActivity.class));
            }
        });
    }

}
