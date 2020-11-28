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

import com.example.rec_commend.MainActivity;
import com.example.rec_commend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 */
public class SongListFragment extends Fragment {

    //UI elements
    private RecyclerView recyclerView;
    private ImageButton myVoiceBtn;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "jsonData";

    private String jsonData;
    private final Map<String, Double> voiceTimbreNorm = new HashMap<String, Double>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongListFragment() {}

    public static SongListFragment newInstance(String jsonData) {
        SongListFragment fragment = new SongListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, jsonData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jsonData = getArguments().getString(ARG_PARAM1);
        }
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_song_list, container, false);
        myVoiceBtn = root.findViewById(R.id.my_voice_btn);

        ArrayList<SongListItem> songList = new ArrayList<SongListItem>();
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

            JSONObject voiceTimbreNormObject = jsonObject.getJSONObject("timbre");
            JSONArray attrs = voiceTimbreNormObject.names();
            for(int i = 0; i < attrs.length(); i++){
                String attr = attrs.getString(i);
                voiceTimbreNorm.put(attr, voiceTimbreNormObject.getDouble(attr));
            }
            System.out.println(voiceTimbreNorm);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        myVoiceBtn.setOnClickListener((view)->{
            int[] rgb = colorMapping(voiceTimbreNorm);
            Bundle bundle = new Bundle();
            bundle.putInt("r", rgb[0]);
            bundle.putInt("g", rgb[1]);
            bundle.putInt("b", rgb[2]);
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

    private int[] colorMapping(Map<String, Double> timbre){
        double depth = Math.max(Math.min(timbre.get("depth"), 1), 0);
        double brightness = Math.max(Math.min(timbre.get("brightness"), 1), 0);
        double roughness = Math.max(Math.min(timbre.get("roughness"), 1), 0);
        double warmth = Math.max(Math.min(timbre.get("warmth"), 1), 0);
        double sharpness = Math.max(Math.min(timbre.get("sharpness"), 1), 0);
        double boominess = Math.max(Math.min(timbre.get("boominess"), 1), 0);
        int r = (int) (roughness * 225 + 15 + warmth * 15);
        int g = (int) (sharpness * 225 + 15 + brightness * 15);
        int b = (int) (boominess * 225 + 15 + depth * 15);
        return new int[]{r, g, b};
    }
}