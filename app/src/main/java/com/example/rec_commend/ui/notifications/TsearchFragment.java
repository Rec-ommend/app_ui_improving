package com.example.rec_commend.ui.notifications;

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

public class TsearchFragment extends Fragment {

    private TsearchViewModel tsearchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tsearchViewModel =
                new ViewModelProvider(this).get(TsearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tsearch, container, false);

        return root;
    }
}