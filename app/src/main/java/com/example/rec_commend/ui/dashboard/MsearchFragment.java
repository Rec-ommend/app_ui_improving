package com.example.rec_commend.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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