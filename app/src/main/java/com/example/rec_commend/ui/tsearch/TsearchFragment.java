package com.example.rec_commend.ui.tsearch;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;

import com.example.rec_commend.MultipartUtility;
import com.example.rec_commend.R;
import com.example.rec_commend.ui.SearchFragment;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TsearchFragment extends SearchFragment {

    @Override
    protected void setDescription() {
        descriptionText.setText(R.string.description_tsearch);
        searchMode = "T";
    }

    @Override
    protected void setPostURL() {
        urlString = "https://timbre-cx7hn2quva-uc.a.run.app/song";
    }

    @Override
    protected List<String> setPostDataAndRequest(String filePath) throws IOException {
        MultipartUtility multipart = new MultipartUtility(urlString, "UTF-8");

        multipart.addFilePart("voice", new File(filePath));

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String[] genreArr = (String[]) preferences.getStringSet("genre", new HashSet<String>()).toArray(new String[0]);
        String genres = String.join("', '", genreArr);
        genres = "['" + genres + "']";
        System.out.println(genres);
        multipart.addFormField("genre", genres);
        multipart.addFormField("start", preferences.getString("start_year", "2000"));
        multipart.addFormField("end", preferences.getString("end_year", "2020"));

        return multipart.finish();
    }
}