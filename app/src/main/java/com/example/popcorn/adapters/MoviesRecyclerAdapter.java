package com.example.popcorn.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.popcorn.adapters.viewholders.ExhaustedViewHolder;
import com.example.popcorn.adapters.viewholders.LoadingViewHolder;
import com.example.popcorn.adapters.viewholders.MoviesViewHolder;
import com.example.popcorn.databinding.ExhaustedListItemBinding;
import com.example.popcorn.databinding.LoadingListItemBinding;
import com.example.popcorn.databinding.MoviesListItemBinding;
import com.example.popcorn.models.api.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListPreloader.PreloadModelProvider<String> {

    private List<Movie> moviesList;
    private RequestManager requestManager;
    private OnItemClickListener listener;
    private ViewPreloadSizeProvider<String> preloadSizeProvider;

    private static final int MOVIE_TYPE = 0;
    private static final int LOADING_TYPE = 1;
    private static final int EXHAUSTED_TYPE = 2;


    public MoviesRecyclerAdapter(List<Movie> moviesList , OnItemClickListener listener, RequestManager requestManager, ViewPreloadSizeProvider provider) {
        this.moviesList = moviesList;
        this.listener = listener;
        this.requestManager = requestManager;
        this.preloadSizeProvider = provider;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case MOVIE_TYPE:
                MoviesListItemBinding moviesListItemBinding = MoviesListItemBinding.inflate(inflater, parent, false);
                return new MoviesViewHolder(moviesListItemBinding, listener, requestManager, preloadSizeProvider);
            case LOADING_TYPE:
                LoadingListItemBinding loadingBinding = LoadingListItemBinding.inflate(inflater, parent, false);
                return new LoadingViewHolder(loadingBinding);
            case EXHAUSTED_TYPE:
                ExhaustedListItemBinding exhaustedListItemBinding = ExhaustedListItemBinding.inflate(inflater, parent, false);
                return new ExhaustedViewHolder(exhaustedListItemBinding);
            default:
                MoviesListItemBinding binding = MoviesListItemBinding.inflate(inflater, parent, false);
                return new MoviesViewHolder(binding, listener, requestManager, preloadSizeProvider);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (moviesList.get(position).getTitle().equals("LOADING")) {
            return LOADING_TYPE;
        } else if (moviesList.get(position).getTitle().equals("EXHAUSTED")) {
            return EXHAUSTED_TYPE;
        } else {
            return MOVIE_TYPE;
        }
    }

    public void setMoviesList(List<Movie> list) {
        this.moviesList = list;
        notifyDataSetChanged();
    }

    public void displayOnlyLoading() {
        clearRecipesList();
        Movie movie = new Movie();
        movie.setTitle("LOADING");
        moviesList.add(movie);
        notifyDataSetChanged();
    }

    public void displayPaginationLoading() {
        if (moviesList == null) {
            moviesList = new ArrayList<>();
        }
        if (!isLoading()) {
            Movie movie = new Movie();
            movie.setTitle("LOADING");
            moviesList.add(movie);
            notifyDataSetChanged();
        }
    }

    public void hideLoading() {
        if (isLoading()) {
            if (moviesList.get(0).getTitle().equals("LOADING")) {
                moviesList.remove(0);
            } else if (moviesList.get(moviesList.size() - 1).getTitle().equals("LOADING")) {
                moviesList.remove(moviesList.size() - 1);
            }
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (moviesList == null) {
            moviesList = new ArrayList<>();
        }
        if (moviesList.size() > 0) {
            return moviesList.get(moviesList.size() - 1).getTitle().equals("LOADING");
        }
        return false;
    }

    private void clearRecipesList() {
        if (moviesList == null) {
            moviesList = new ArrayList<>();
        } else {
            moviesList.clear();
        }
        notifyDataSetChanged();
    }

    public void setQueryExhausted() {
        hideLoading();
        Movie movie = new Movie();
        movie.setTitle("EXHAUSTED");
        moviesList.add(movie);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //if there was more than one , we would have compared it with each type
        int itemViewType = getItemViewType(position);
        if (itemViewType == MOVIE_TYPE) {
            ((MoviesViewHolder) holder).onBind(moviesList.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    @NonNull
    @Override
    public List<String> getPreloadItems(int position) {
        String url = moviesList.get(position).getPosterPath();
        if (TextUtils.isEmpty(url)) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(url);
        }
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull String item) {
        return requestManager.load(item);
    }
}
