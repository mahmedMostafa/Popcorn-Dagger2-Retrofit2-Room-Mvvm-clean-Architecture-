package com.example.popcorn.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.popcorn.models.api.Cast;
import com.example.popcorn.models.api.Movie;
import com.example.popcorn.models.response.CastApiResponse;
import com.example.popcorn.models.response.MoviesApiResponse;
import com.example.popcorn.persistence.MoviesDao;
import com.example.popcorn.remote.ApiResponse;
import com.example.popcorn.remote.NetworkBoundResource;
import com.example.popcorn.remote.Resource;
import com.example.popcorn.remote.api.MovieApi;
import com.example.popcorn.util.C;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieRepository {

    private static final String TAG = "MovieRepository";

    private MoviesDao moviesDao;
    private MovieApi movieApi;

    @Inject
    public MovieRepository(MoviesDao moviesDao, MovieApi movieApi) {
        this.moviesDao = moviesDao;
        this.movieApi = movieApi;
    }

    public LiveData<Resource<List<Movie>>> getMovies(final int pageNumber) {

        return new NetworkBoundResource<List<Movie>, MoviesApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull MoviesApiResponse item) {
                Log.d(TAG, "saveCallResult: CALLED");
                //here i'm inserting all the movies and in case of a conflict (-1) , i just update the basic info without the other lists like
                //actors and genres as they would be deleted if it was updated manually
                List<Movie> movies = item.getResults();
                int i = 0;
                for (long rowId : moviesDao.insertMovies(movies)) {
                    if (rowId == -1) {
                        Log.d(TAG, "saveCallResult: CONFLICT .. this movie is already in the cache");
                        moviesDao.updateMovie(
                                movies.get(i).getId(),
                                movies.get(i).getTitle(),
                                movies.get(i).getVoters(),
                                movies.get(i).getPosterPath(),
                                movies.get(i).getDescription(),
                                movies.get(i).getReleaseDate()
                        );
                    } else {
                        Log.d(TAG, "saveCallResult: Movie " + movies.get(i).getTitle() + " is Added to the cache");
                    }
                    i++;
                }
            }

            @Override
            protected boolean shouldFetch(List<Movie> movies) {
                Log.d(TAG, "shouldFetch: CALLED");
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Movie>> loadFromDb() {
                Log.d(TAG, "loadFromDb: CALLED");
                return moviesDao.getMovies(Long.parseLong(String.valueOf(pageNumber)));
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MoviesApiResponse>> createCall() {
                Log.d(TAG, "Movies createCall: CALLED");
                return movieApi.getPopularMovies(
                        pageNumber
                );
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<Movie>> getCastDetails(final String movieId) {
        return new NetworkBoundResource<Movie, CastApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull CastApiResponse item) {
                List<Cast> casts = Arrays.asList(item.getCasts());
                moviesDao.updateMovieCast(Long.parseLong(movieId), casts);
            }

            @Override
            protected boolean shouldFetch(Movie movie) {
                return true;
                //return shouldRefresh(movie);
            }

            @NonNull
            @Override
            protected LiveData<Movie> loadFromDb() {
                Log.d(TAG, "loadFromDb: CALLED");
                return moviesDao.getMovieById(Long.valueOf(movieId));
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<CastApiResponse>> createCall() {
                Log.d(TAG, "Cast createCall: CALLED");
                return movieApi.getCastDetails(movieId);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<Movie>> getMovieDetails(final String movieId) {
        return new NetworkBoundResource<Movie, Movie>() {

            @Override
            protected void saveCallResult(@NonNull Movie item) {
                item.setTimestamp((int) (System.currentTimeMillis() / 1000));
                moviesDao.insertMovie(item);
            }

            @Override
            protected boolean shouldFetch(Movie movie) {
                return true;
                //return shouldRefresh(movie);
            }

            @NonNull
            @Override
            protected LiveData<Movie> loadFromDb() {
                Log.d(TAG, "loadFromDb: CALLED");
                return moviesDao.getMovieById(Long.parseLong(movieId));
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Movie>> createCall() {
                return movieApi.getMovieDetails(movieId);
            }
        }.getAsLiveData();
    }

    private boolean shouldRefresh(Movie movie) {
        int lastTime = movie.getTimestamp();
        int currentTime = (int) System.currentTimeMillis() / 1000;
        Log.d(TAG, "shouldFetch: it's been " + ((currentTime - lastTime) / 60 / 60 / 24) + "days since last refresh");
        Log.d(TAG, "shouldFetch: 10 days must elapse");
        if ((currentTime - lastTime) >= C.MOVIE_REFRESH_TIME) {
            Log.d(TAG, "shouldFetch: Should refresh ?" + true);
            return true;
        }
        Log.d(TAG, "shouldFetch: Should refresh ?" + false);
        return false;
    }
}
