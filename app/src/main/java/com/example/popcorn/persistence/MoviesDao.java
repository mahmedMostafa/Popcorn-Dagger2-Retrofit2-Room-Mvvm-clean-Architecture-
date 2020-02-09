package com.example.popcorn.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.popcorn.models.api.Cast;
import com.example.popcorn.models.api.Movie;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MoviesDao {

    @Insert(onConflict = IGNORE)
    long[] insertMovies(List<Movie> movies);

    @Insert(onConflict = REPLACE)
    void insertMovie(Movie movie);

    @Query("UPDATE movies SET title = :title , voters = :voters , poster_path = :posterPath , description = :description , release_date = :releaseDate " +
            "WHERE id = :movieId ")
    void updateMovie(Long movieId , String title , int voters , String posterPath , String description , String releaseDate);

    @Query("UPDATE movies SET casts = :casts WHERE id = :movieId")
    void updateMovieCast(Long movieId , List<Cast> casts);

    @Query("SELECT * FROM movies WHERE id = :movieId")
    LiveData<Movie> getMovieById(Long movieId);

    @Query("SELECT * FROM movies LIMIT (:page * 30) ")
    LiveData<List<Movie>> getMovies(Long page);

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' LIMIT (:page * 30)")
    LiveData<List<Movie>> searchMovies(String query,int page);



}
