package com.example.rec_commend.ui.song_list;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rec_commend.R;

import java.util.ArrayList;

public class SongListRecyclerViewAdapter extends RecyclerView.Adapter<SongListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<SongListItem> mValues;

    public SongListRecyclerViewAdapter(ArrayList<SongListItem> items) { mValues = items; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIndexView.setText(Integer.toString(position + 1));
        holder.mTitleView.setText(mValues.get(position).title);
        holder.mInfoView.setText(mValues.get(position).info);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIndexView;
        public final TextView mTitleView;
        public final TextView mInfoView;
        public SongListItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIndexView = (TextView) view.findViewById(R.id.index);
            mTitleView = (TextView) view.findViewById(R.id.song_title);
            mInfoView = (TextView) view.findViewById(R.id.song_info);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}