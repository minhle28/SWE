package com.example.visualock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private List<String> imageNames;
    private List<String> imageUrls;

    public ImageAdapter(Context context, List<String> imageNames, List<String> imageUrls) {
        this.context = context;
        this.imageNames = imageNames;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // Load image into ImageView using Picasso
        Picasso.get().load(imageUrls.get(position)).into(holder.imageView);

        // Set image name
        String imageName = imageNames.get(position);
        if (imageName.length() > 16) {
            // If the name is longer than 16 characters, truncate it
            imageName = imageName.substring(0, 13) + "...";
        }
        holder.textViewName.setText(imageName);

        // Delete button click listener
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.materialSwitch.isChecked()) {
                    // If toggle is on, show a dialog
                    showToggleAlertDialog();
                } else {
                    // If toggle is off, proceed with deletion
                    removeItem(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        ImageView deleteButton;
        SwitchMaterial materialSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            materialSwitch = itemView.findViewById(R.id.material_switch);
        }
    }

    private void removeItem(int position) {
        // Remove the image name and URL from the lists
        imageNames.remove(position);
        imageUrls.remove(position);

        // Notify adapter about the removal
        notifyDataSetChanged();
    }

    private void showToggleAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning");
        builder.setMessage("Toggle is ON which is your current password. \nPlease turn it OFF before deleting.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
