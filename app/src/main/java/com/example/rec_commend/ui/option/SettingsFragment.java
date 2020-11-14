package com.example.rec_commend.ui.option;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.rec_commend.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}