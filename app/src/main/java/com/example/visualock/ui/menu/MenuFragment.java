package com.example.visualock.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.content.Intent;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.visualock.AboutActivity;
import com.example.visualock.GraphLoginActivity;
import com.example.visualock.ProfileActivity;
import com.example.visualock.R;

import com.example.visualock.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuFragment extends Fragment {

    private ListView mMenuListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        mMenuListView = root.findViewById(R.id.list_menu);

        String[] options = {"Profile", "About Us", "Setting", "Logout"};
        int[] icons = {
                R.drawable.user_24,
                R.drawable.info_24,
                R.drawable.setting_24,
                R.drawable.logout_24
        };

        CustomListAdapter adapter = new CustomListAdapter(requireContext(), options, icons);
        mMenuListView.setAdapter(adapter);

        mMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent profile = new Intent(requireContext(), ProfileActivity.class);
                        startActivity(profile);
                        break;
                    case 1:
                        Intent about = new Intent(requireContext(), AboutActivity.class);
                        startActivity(about);
                        break;
                    case 2:
                        Intent setting = new Intent(requireContext(), SettingActivity.class);
                        startActivity(setting);
                        break;
                    case 3:
                        FirebaseAuth.getInstance().signOut();
                        Intent logout = new Intent(requireContext(), GraphLoginActivity.class);
                        startActivity(logout);
                        requireActivity().finish();
                        break;
                }
            }
        });

        return root;
    }
}

