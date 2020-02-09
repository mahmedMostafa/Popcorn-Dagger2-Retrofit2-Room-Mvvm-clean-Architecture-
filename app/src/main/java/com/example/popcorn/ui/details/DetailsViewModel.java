package com.example.popcorn.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.popcorn.models.api.Movie;
import com.example.popcorn.remote.Resource;
import com.example.popcorn.repositories.MovieRepository;

import javax.inject.Inject;

public class DetailsViewModel extends ViewModel {

    private MovieRepository movieRepository;

    @Inject
    public DetailsViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public LiveData<Resource<Movie>> getCasts(String movieId){
        return movieRepository.getCastDetails(movieId);
    }

    public LiveData<Resource<Movie>> getMovieDetails(String movieId) {
        return movieRepository.getMovieDetails(movieId);
    }

}
