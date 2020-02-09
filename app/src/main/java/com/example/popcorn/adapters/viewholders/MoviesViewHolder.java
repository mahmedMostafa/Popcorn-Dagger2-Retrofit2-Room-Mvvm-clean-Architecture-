package com.example.popcorn.adapters.viewholders;

import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.popcorn.adapters.OnItemClickListener;
import com.example.popcorn.databinding.MoviesListItemBinding;
import com.example.popcorn.models.api.Movie;

public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private RequestManager requestManager;
    private MoviesListItemBinding binding; //TODO make use of data binding here
    private ViewPreloadSizeProvider<String> preloadSizeProvider;
    private OnItemClickListener listener;
    private Movie currentMovie;

    public MoviesViewHolder(MoviesListItemBinding binding, OnItemClickListener listener ,
                            RequestManager requestManager ,ViewPreloadSizeProvider<String> preloadSizeProvider) {
        super(binding.getRoot());
        this.binding = binding;
        this.requestManager = requestManager;
        this.listener = listener;
        this.preloadSizeProvider = preloadSizeProvider;
        binding.getRoot().setOnClickListener(this);
       // title = itemView.findViewById(R.id.item_tile);
        //imageView = itemView.findViewById(R.id.item_image);
    }

    public void onBind(Movie movie){
        currentMovie = movie;
        binding.itemTile.setText(movie.getTitle());
        requestManager.load(movie.getPosterPath()).into(binding.itemImage);
        preloadSizeProvider.setView(binding.itemImage);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if(listener != null){
            ViewCompat.setTransitionName(binding.itemImage,currentMovie.getPosterPath());
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position , binding.itemImage);
            }
        }
    }
}
