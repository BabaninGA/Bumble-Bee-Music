package com.example.mp3player;

import java.io.File;
import java.util.ArrayList;

public class Playlist {
    String name;
    ArrayList<Song> songs;

    public Playlist(){
        this.name = "";
        this.songs=new ArrayList<Song>();
    }

    public Playlist(String name, ArrayList<Song> songs){
        this.name = name;
        this.songs = new ArrayList<Song>();
    }

    public Playlist(File dir){

        this.name = dir.getName();
        File[] song_files = dir.listFiles();
        this.songs = new ArrayList<>();
        assert song_files != null;
        for (File f: song_files){
            boolean endcheck = f.toString().endsWith("mp3");
            if((!f.isDirectory()) &(endcheck == true)) {
                Song s = new Song(f);
                this.songs.add(s);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }
}