package com.example.rec_commend.ui.song_list;

public class SongListItem {
    public String id;
    public String title;
    public String singer;
    public String genre;
    public String release;
    public double timbre_similarity;

    public SongListItem (String id, String title, String singer, String genre, String release, double timbre_similarity){
        this.id = id;
        this.title = title;
        this.singer = singer;
        this.genre = genre;
        this.release = release;
        this.timbre_similarity = timbre_similarity;
    }

}
