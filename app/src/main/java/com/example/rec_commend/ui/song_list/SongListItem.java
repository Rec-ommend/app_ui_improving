package com.example.rec_commend.ui.song_list;

public class SongListItem {
    public String id;
    public String title;
    public String info;
    public double timbre_similarity;

    public SongListItem (String id, String title, String singer, String genre, String release, double timbre_similarity){
        this.id = id;
        this.title = title + " - " + singer;
        this.info = "장르 : " + genre + " | 발매일 : " + release;
        this.timbre_similarity = timbre_similarity;
    }

}
