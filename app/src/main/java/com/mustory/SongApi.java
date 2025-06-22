// SongApi.java
package com.mustory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SongApi {
    @GET("get_songs.php")
    Call<List<Song>> getAllSongs();

}


