package com.example.mp3player;

import java.io.File;


public class Song {
    String general_name;
    String name;
    String author;
    String path;

    public Song(File file) {
        this.path = file.getPath();
        String filename = file.getName();
        if (filename.contains("-")) {
            String[] splitted_filename = filename.split("[-.]");
            this.name = splitted_filename[1].trim();
            this.author = splitted_filename[0].trim();
            this.general_name = this.author + " - " + this.name;
        }else {
            this.name = filename.split("\\.")[0];
            this.general_name = this.name;
            this.author = "";
        }

    }

    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGeneral_name() {
        return general_name;
    }
}
