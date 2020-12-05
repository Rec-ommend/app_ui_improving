package com.example.rec_commend.ui.option;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rec_commend.R;
import com.example.rec_commend.ui.home.HomeViewModel;

public class OptionFragment extends Fragment {

    //UI elements
    private CardView noticeBtn;
    private CardView preferenceBtn;
    private CardView helpBtn;
    private CardView versionBtn;
    private CardView helpUsBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_option, container, false);
        noticeBtn = root.findViewById(R.id.notice_btn);
        preferenceBtn = root.findViewById(R.id.preference_btn);
        helpBtn = root.findViewById(R.id.help_btn);
        versionBtn = root.findViewById(R.id.version_btn);
        helpUsBtn = root.findViewById(R.id.help_us_btn);

        preferenceBtn.setOnClickListener((view)-> Navigation.findNavController(view).navigate(R.id.navigation_preference));
        return root;
    }
}