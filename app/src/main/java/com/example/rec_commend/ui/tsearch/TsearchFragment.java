package com.example.rec_commend.ui.tsearch;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.example.rec_commend.R;
import com.example.rec_commend.ui.SearchFragment;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TsearchFragment extends SearchFragment {

    @Override
    protected void setDescription() {
        descriptionText.setText("노래의 일부를 들려주면\n그 노래와 유사한 노래를 찾아줍니다.");
    }

    @Override
    protected void setPostURL() {
        urlString = "https://timbre-cx7hn2quva-uc.a.run.app/voice";
    }
}