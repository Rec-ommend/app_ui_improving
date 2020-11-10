package com.example.rec_commend.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.rec_commend.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ImageButton msearch_btn;
    private ImageButton tsearch_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        msearch_btn = root.findViewById(R.id.msearch_btn);
        msearch_btn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.navigation_msearch);
        });

        tsearch_btn = root.findViewById(R.id.tsearch_btn);
        tsearch_btn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.navigation_tsearch);
        });

        return root;
    }
}