package com.example.rec_commend.ui.msearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rec_commend.R;

public class MsearchFragment extends Fragment {

    private MsearchViewModel msearchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        msearchViewModel =
                new ViewModelProvider(this).get(MsearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_msearch, container, false);
        
        return root;
    }
}