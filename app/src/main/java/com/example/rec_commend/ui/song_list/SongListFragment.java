package com.example.rec_commend.ui.song_list;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rec_commend.MainActivity;
import com.example.rec_commend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class SongListFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "jsonData";

    private String jsonData;
    private ArrayList<SongListItem> songList;

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
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        songList = new ArrayList<SongListItem>();
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                songList.add(new SongListItem(
                        jsonObject.getString("id"),
                        jsonObject.getString("title"),
                        jsonObject.getString("singer"),
                        jsonObject.getString("genre"),
                        jsonObject.getString("release"),
                        jsonObject.getDouble("timbre_similarity")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new SongListRecyclerViewAdapter(songList));
        }
        return view;
    }
}