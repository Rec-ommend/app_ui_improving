package com.example.rec_commend.ui.msearch;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.example.rec_commend.R;
import com.example.rec_commend.ui.SearchFragment;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MsearchFragment extends SearchFragment {

    @Override
    protected void setDescription() {
        descriptionText.setText(R.string.description_msearch);
    }

    @Override
    protected void setPostURL() {
        urlString = "https://timbre-cx7hn2quva-uc.a.run.app/voice";
    }
}