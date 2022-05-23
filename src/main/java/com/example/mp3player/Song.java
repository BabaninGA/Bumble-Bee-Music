package com.example.mp3player;

import java.io.File;


public class Song {
    String general_name;
    String name;
    String author;
    File file;
    String path;

    public Song(File file) {
        this.file = file;
        this.path = file.getPath();
        String filename = file.getName();
        if (filename.contains("-")) {
            String[] splitted_filename = filename.split("[-\\.]");
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

    public void setName(String name) {
        this.name = name;
    }

    public String getGeneral_name() {
        return general_name;
    }

    public void setGeneral_name(String general_name) {
        this.general_name = general_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
