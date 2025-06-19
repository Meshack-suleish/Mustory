package com.mustory;

public class Songs {
    String songName;
    String songPath;
    String description;

    public Songs() {}

    public Songs(String songName, String songPath, String description) {
        this.songName = songName;
        this.songPath = songPath;
        this.description = description;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongPath() {
        return songPath;
    }

    public String getDescription() {
        return description;
    }
}