package com.eightbytestech.moviesplusv2.utility;

import com.eightbytestech.moviesplusv2.model.MoviesInfo;
import com.eightbytestech.moviesplusv2.model.ReviewInfo;
import com.eightbytestech.moviesplusv2.model.TrailerInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDBEndpointInterface {

    @GET ("movie/popular")
    Call<MoviesInfo> getPopularMovies(
            @Query(value = "api_key", encoded = true) String apiKey,
            @Query(value = "language", encoded = true) String language
    );

    //movie/top_rated
    @GET ("movie/top_rated")
    Call<MoviesInfo> getTopRatedMovies(
            @Query(value = "api_key", encoded = true) String apiKey,
            @Query(value = "language", encoded = true) String language
    );

    //movies videos
    @GET ("movie/{id}/videos")
    Call<TrailerInfo> getMovieVideos(
            @Path("id") int id,
            @Query(value = "api_key", encoded = true) String apiKey,
            @Query(value = "language", encoded = true) String language
    );

    //movies review
    @GET ("movie/{id}/reviews")
    Call<ReviewInfo> getMovieReviews(
            @Path("id") int id,
            @Query(value = "api_key", encoded = true) String apiKey,
            @Query(value = "language", encoded = true) String language
    );

}
