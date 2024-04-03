package com.example.visualock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private int[] imageIds;

    public ImageAdapter(Context context, int[] imageIds) {
        this.context = context;
        this.imageIds = imageIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // Load image into ImageView
        holder.imageView.setImageResource(imageIds[position]);

        // Set image name
        String imageName = "Image " + (position + 1);
        holder.textViewName.setText(imageName);

        // Delete button click listener
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete image logic here
                // For demonstration, you can remove the image from the list
                // and notify the adapter
                // You can implement your actual deletion logic here
                // For example, if you store image IDs in a database, you would delete
                // the corresponding record from the database
                removeItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageIds.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private void removeItem(int position) {
        // Remove the image from the array
        int[] newArray = new int[imageIds.length - 1];
        System.arraycopy(imageIds, 0, newArray, 0, position);
        System.arraycopy(imageIds, position + 1, newArray, position, imageIds.length - position - 1);
        imageIds = newArray;

        // Notify adapter about the removal
        notifyDataSetChanged();
    }
}
