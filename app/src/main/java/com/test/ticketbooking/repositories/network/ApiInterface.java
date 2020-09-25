package com.test.ticketbooking.repositories.network;




import com.test.ticketbooking.models.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("now_playing")
    Call<Results> getData(@Query("api_key") String api_key,
                          @Query("language") String language,
                          @Query("page") String page);

    @GET("{movie_id}/similar")
    Call<Results> getSimilarData(@Path("movie_id") String movie_id,
                          @Query("api_key") String api_key,
                          @Query("language") String language,
                          @Query("page") String page);
}
