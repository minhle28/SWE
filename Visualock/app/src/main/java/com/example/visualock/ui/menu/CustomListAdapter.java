package com.example.visualock.ui.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.visualock.R;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private final String[] mOptions;
    private final int[] mIcons;

    public CustomListAdapter(Context context, String[] options, int[] icons) {
        super(context, R.layout.menu_item, options);
        mContext = context;
        mOptions = options;
        mIcons = icons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.menu_item, null, true);

        TextView textView = view.findViewById(R.id.text_menu);
        ImageView imageView = view.findViewById(R.id.icon_menu);

        textView.setText(mOptions[position]);
        imageView.setImageResource(mIcons[position]);

        return view;
    }
}
