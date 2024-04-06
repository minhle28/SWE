package com.example.visualock;

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
import android.widget.ListView;
import android.widget.Toast;
import android.graphics.Color;

public class GraphPasswordActivity extends AppCompatActivity {
    private static int lastClickedPosition = -1;
    private static final int MAX_CLICKS = 5; // Maximum allowed clicks
    private static int totalClicks = 0;

    public class PasswordImageAdapter extends BaseAdapter {
        // Other methods and fields

        private Context mContext;
        private int[] mImageIds;

        private boolean[] clickedStates;
        private int numberOfClickedImages = 0;

        public PasswordImageAdapter(Context context, int[] imageIds) {
            mContext = context;
            mImageIds = imageIds;
            clickedStates = new boolean[mImageIds.length];
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mImageIds[position]);

            // Apply transparent grey overlay if the image is clicked
            if (clickedStates[position]) {
                imageView.setColorFilter(Color.parseColor("#80000000"));
            } else {
                imageView.setColorFilter(null);
            }

            // Toggle clicked state on click
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check if the maximum number of clicks is reached
                    if (totalClicks >= MAX_CLICKS && !clickedStates[position]) {
                        Toast.makeText(mContext, "You can only select up to 5 images.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Toggle the clicked state
                    if (clickedStates[position]) {
                        clickedStates[position] = false;
                        totalClicks--;
                    } else {
                        clickedStates[position] = true;
                        totalClicks++;
                    }

                    notifyDataSetChanged(); // Refresh the adapter to update the UI
                }
            });

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
        Tree.setAdapter(new PasswordImageAdapter(this, treeImages));
        ListView Color = findViewById(R.id.cat_color);
        Color.setAdapter(new PasswordImageAdapter(this, colorImages));
        ListView dailyObjects = findViewById(R.id.cat_dailyObjects);
        dailyObjects.setAdapter(new PasswordImageAdapter(this,dailyObjectsImages));
        ListView Animals = findViewById(R.id.cat_animals);
        Animals.setAdapter(new PasswordImageAdapter(this, animalImages));
        ListView Places = findViewById(R.id.cat_places);
        Places.setAdapter(new PasswordImageAdapter(this, placesImages));
        ListView Vehicle = findViewById(R.id.cat_vehicles);
        Vehicle.setAdapter(new PasswordImageAdapter(this,vehicleImages));

        Tree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GraphPasswordActivity.this, "Tree image clicked at position: " + position, Toast.LENGTH_SHORT).show();
                lastClickedPosition = position;
                ((BaseAdapter) parent.getAdapter()).notifyDataSetChanged(); // Refresh ListView
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
