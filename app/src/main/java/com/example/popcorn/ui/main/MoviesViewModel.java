package com.example.popcorn.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.popcorn.models.api.Movie;
import com.example.popcorn.remote.Resource;
import com.example.popcorn.repositories.MovieRepository;

import java.util.List;

import javax.inject.Inject;

import static com.example.popcorn.util.C.QUERY_EXHAUSTED;

public class MoviesViewModel extends ViewModel {

    private static final String TAG = "MoviesViewModel";


    private MediatorLiveData<Resource<List<Movie>>> movies = new MediatorLiveData<>();
    private MovieRepository movieRepository;

    //query extras
    private boolean isQueryExhausted;
    private boolean isPerformingQuery;
    private int pageNumber;
    private boolean cancelRequest;
    private long requestStartTime;


    @Inject
    public MoviesViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public LiveData<Resource<List<Movie>>> getMovies() {
        return movies;
    }

    public void searchMovies(final int pageNumber) {
        if (!isPerformingQuery) {
            this.pageNumber = pageNumber;
            isQueryExhausted = false;
            executeSearch();
        }
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void searchNextPage() {
        if (!isQueryExhausted && !isPerformingQuery) {
            pageNumber++;
            executeSearch();
        }
    }

    private void executeSearch() {
        requestStartTime = System.currentTimeMillis();
        cancelRequest = false;
        isPerformingQuery = true;
        final LiveData<Resource<List<Movie>>> source = movieRepository.getMovies(pageNumber);
        //Just remember to remove the source in all the cases so that it doesn't keep listening
        movies.addSource(source, new Observer<Resource<List<Movie>>>() {
            @Override
            public void onChanged(Resource<List<Movie>> listResource) {
                if (!cancelRequest) {
                    if (listResource != null) {
                        movies.setValue(listResource);
                        if (listResource.status == Resource.Status.SUCCESS) {
                            Log.d(TAG, "onChanged: REQUEST TIME :" + (System.currentTimeMillis() - requestStartTime) / 1000 + " Seconds");
                            isPerformingQuery = false; // because it's done querying the data
                            if (listResource.data != null) {
                                if (listResource.data.size() == 0) {
                                    Log.d(TAG, "onChanged: query is exhausted");
                                    movies.setValue(new Resource<List<Movie>>(
                                            Resource.Status.ERROR,
                                            listResource.data,
                                            QUERY_EXHAUSTED
                                    ));
                                }
                            }
                            movies.removeSource(source);
                        } else if (listResource.status == Resource.Status.ERROR) {
                            Log.d(TAG, "onChanged: REQUEST TIME :" + (System.currentTimeMillis() - requestStartTime) / 1000 + " Seconds");
                            isPerformingQuery = false;
                            movies.removeSource(source);
                        }
                    } else {
                        movies.removeSource(source);
                    }
                }else{
                    movies.removeSource(source);
                }
            }
        });
    }

    public void cancelSearchRequest() {
        if (isPerformingQuery) {
            Log.d(TAG, "cancelSearchRequest: cancelling search request");
            cancelRequest = true;
            isPerformingQuery = true;
            pageNumber = 1;
        }
    }
}
