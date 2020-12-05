package com.example.rec_commend.ui.song_list;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rec_commend.MainActivity;
import com.example.rec_commend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 */
public class SongListFragment extends Fragment {

    //UI elements
    private RecyclerView recyclerView;
    private ImageButton resultBtn;
    private TextView resultDescription;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "jsonData";
    private static final String ARG_PARAM2 = "searchMode";

    private String jsonData;
    private String searchMode;
    private final Map<String, Double> timbreNorm = new HashMap<>();
    private final Map<String, Double> timbre = new HashMap<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongListFragment() {}

    public static SongListFragment newInstance(String _jsonData, String _searchMode) {
        SongListFragment fragment = new SongListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, _jsonData);
        args.putString(ARG_PARAM2, _searchMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jsonData = getArguments().getString(ARG_PARAM1);
            searchMode = getArguments().getString(ARG_PARAM2);
        }
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_song_list, container, false);

        resultDescription = root.findViewById(R.id.result_desc_text);
        if(searchMode.equals("M")) // Music search
            resultDescription.setText(R.string.result_description_m);
        else // searchMode == "T" // Tune search
            resultDescription.setText(R.string.result_description_t);

        resultBtn = root.findViewById(R.id.result_btn);

        ArrayList<SongListItem> songList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            JSONArray songArray = new JSONArray(jsonObject.getString("song"));
            for(int i = 0; i < songArray.length(); i++){
                JSONObject jsonSong = (JSONObject) songArray.get(i);
                songList.add(new SongListItem(
                        jsonSong.getString("id"),
                        jsonSong.getString("title"),
                        jsonSong.getString("singer"),
                        jsonSong.getString("genre"),
                        jsonSong.getString("release"),
                        jsonSong.getDouble("timbre_similarity")
                ));
            }

            JSONObject timbreNormObject = jsonObject.getJSONObject("timbre");
            JSONArray attrs = timbreNormObject.names();
            for(int i = 0; i < attrs.length(); i++){
                String attr = attrs.getString(i);
                timbreNorm.put(attr, timbreNormObject.getDouble(attr));
            }
            System.out.println(timbreNorm);

            JSONObject timbreObject = jsonObject.getJSONObject("origin");
            for(int i = 0; i < attrs.length(); i++){
                String attr = attrs.getString(i);
                timbre.put(attr, timbreObject.getDouble(attr));
            }
            System.out.println(timbre);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        resultBtn.setOnClickListener((view)->{
            Bundle bundle = new Bundle();
            bundle.putString("searchMode", searchMode);
            bundle.putSerializable("timbre", (Serializable) timbre);
            bundle.putSerializable("timbreNorm", (Serializable) timbreNorm);
            Navigation.findNavController(view).navigate(R.id.navigation_share, bundle);
        });


        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }

        // Set the adapter
        recyclerView = (RecyclerView) root.findViewById(R.id.song_list);
        recyclerView.setAdapter(new SongListRecyclerViewAdapter(songList));
        return root;
    }

}