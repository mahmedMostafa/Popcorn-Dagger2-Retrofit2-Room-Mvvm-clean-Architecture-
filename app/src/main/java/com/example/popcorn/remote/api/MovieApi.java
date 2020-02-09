package com.example.popcorn.remote.api;

import androidx.lifecycle.LiveData;

import com.example.popcorn.models.api.Movie;
import com.example.popcorn.models.response.CastApiResponse;
import com.example.popcorn.models.response.MoviesApiResponse;
import com.example.popcorn.remote.ApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("movie/{movie_id}")
    LiveData<ApiResponse<Movie>> getMovieDetails(
            @Path("movie_id") String movieId
    );

    @GET("movie/popular")
    LiveData<ApiResponse<MoviesApiResponse>> getPopularMovies(
            @Query("page") int pageNumber
    );

    @GET("movie/{movie_id}/similar")
    LiveData<ApiResponse<MoviesApiResponse>> getSimilarMovies(
            @Path("movie_id") String movieId,
            @Query("page") int pageNumber
    );

    //TODO not done
    @GET("movie/{movie_id}/credits")
    LiveData<ApiResponse<CastApiResponse>> getCastDetails(
            @Path("movie_id") String movieId
    );

    @GET("search/movie")
    LiveData<ApiResponse<MoviesApiResponse>> searchMovies(
            @Query("query") String query,
            @Query("page") int pageNumber
    );
}
