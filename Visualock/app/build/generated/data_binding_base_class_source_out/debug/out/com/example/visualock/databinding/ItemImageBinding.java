// Generated by view binder compiler. Do not edit!
package com.example.visualock.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.visualock.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemImageBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView deleteButton;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final SwitchMaterial materialSwitch;

  @NonNull
  public final TextView textViewName;

  private ItemImageBinding(@NonNull RelativeLayout rootView, @NonNull ImageView deleteButton,
      @NonNull ImageView imageView, @NonNull SwitchMaterial materialSwitch,
      @NonNull TextView textViewName) {
    this.rootView = rootView;
    this.deleteButton = deleteButton;
    this.imageView = imageView;
    this.materialSwitch = materialSwitch;
    this.textViewName = textViewName;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemImageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemImageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_image, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemImageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.deleteButton;
      ImageView deleteButton = ViewBindings.findChildViewById(rootView, id);
      if (deleteButton == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.material_switch;
      SwitchMaterial materialSwitch = ViewBindings.findChildViewById(rootView, id);
      if (materialSwitch == null) {
        break missingId;
      }

      id = R.id.textViewName;
      TextView textViewName = ViewBindings.findChildViewById(rootView, id);
      if (textViewName == null) {
        break missingId;
      }

      return new ItemImageBinding((RelativeLayout) rootView, deleteButton, imageView,
          materialSwitch, textViewName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}