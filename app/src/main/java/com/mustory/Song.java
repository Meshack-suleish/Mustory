package com.mustory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Song implements Parcelable {
    // If your JSON keys are different, annotate as needed
    // For example: @SerializedName("track_name") private String name;

    private String name;
    private String description;
    private String artist;
    private String path;
    private int number;

    // Required by Gson
    public Song() {
    }

    public Song(String name, String description, String artist, String path, int number) {
        this.name = name;
        this.description = description;
        this.artist = artist;
        this.path = path;
        this.number = number;
    }

    protected Song(Parcel in) {
        name = in.readString();
        description = in.readString();
        artist = in.readString();
        path = in.readString();
        number = in.readInt();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    public int getNumber() {
        return number;
    }

    // Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(artist);
        dest.writeString(path);
        dest.writeInt(number);
    }
}
