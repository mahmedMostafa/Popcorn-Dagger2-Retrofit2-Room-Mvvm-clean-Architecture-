package com.example.popcorn.ui.details;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.popcorn.R;
import com.example.popcorn.adapters.CastsRecyclerAdapter;
import com.example.popcorn.databinding.ActivityDetailsBinding;
import com.example.popcorn.factory.ViewModelProviderFactory;
import com.example.popcorn.models.api.Cast;
import com.example.popcorn.models.api.Movie;
import com.example.popcorn.remote.Resource;
import com.example.popcorn.util.C;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class DetailsActivity extends DaggerAppCompatActivity {

    private static final String TAG = "DetailsActivity";

    private ActivityDetailsBinding binding;
    private List<Cast> casts;
    private CastsRecyclerAdapter adapter;


    private String movieId;
    private Intent intent;
    private String transitionName;

    @Inject
    RequestManager requestManager;

    @Inject
    ViewModelProviderFactory factory;

    private DetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        hideFadeAndToolbarFlash();
        viewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel.class);
        intent = getIntent();
        setupRecyclerView();
        movieId = intent.getStringExtra(C.MOVIE_ID_KEY);
        transitionName = intent.getStringExtra(C.IMAGE_ANIMATION_KEY);
        subscribeObservers();
    }

    private void setupRecyclerView() {
        casts = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.castsRecyclerView.setLayoutManager(layoutManager);
        binding.castsRecyclerView.setHasFixedSize(true);
        adapter = new CastsRecyclerAdapter(requestManager, casts);
        binding.castsRecyclerView.setAdapter(adapter);
    }


    private void subscribeObservers() {
        viewModel.getCasts(movieId).observe(this, new Observer<Resource<Movie>>() {
            @Override
            public void onChanged(Resource<Movie> movieResource) {
                switch (movieResource.status) {
                    case SUCCESS: {
                        Log.d(TAG, "onChanged: Success returning casts");
                        if (movieResource.data != null && movieResource.data.getCasts() != null) {
                            adapter.setCasts(movieResource.data.getCasts());
                            casts = movieResource.data.getCasts();
                        }

                        break;
                    }
                    case ERROR: {
                        Log.e(TAG, "onChanged: Error retrieving casts" + movieResource.message);
                        // it can be null if the user didn't select the item so the query was never made
                        if (movieResource.data != null && movieResource.data.getCasts() != null) {
                            adapter.setCasts(movieResource.data.getCasts());
                            casts = movieResource.data.getCasts();
                        }
                        break;
                    }
                    case LOADING: {
                        Log.d(TAG, "onChanged: Loading casts");
                        break;
                    }
                }
            }
        });
        viewModel.getMovieDetails(movieId).observe(this, new Observer<Resource<Movie>>() {
            @Override
            public void onChanged(Resource<Movie> movieResource) {
                switch (movieResource.status) {
                    case SUCCESS: {
                        setProperties(movieResource);
                        break;
                    }

                    case ERROR: {
                        Log.e(TAG, "onChanged: Error receiving cast " + movieResource.message);
                        setProperties(movieResource);
                        break;
                    }

                    case LOADING: {
                        Log.d(TAG, "onChanged: Casts LOADING");
                        //nothing for now
                        break;
                    }
                }
            }
        });
    }

    private void setProperties(Resource<Movie> movieResource) {
        binding.detailsTitleText.setText(movieResource.data.getTitle());
        binding.detailsDateText.setText(movieResource.data.getReleaseDate());
        //requestManager.load(movieResource.data.getPosterPath()).into(binding.detailsPosterImage);
        binding.detailsDescriptionText.setText(movieResource.data.getDescription());
        loadAnimatedImage(movieResource);
    }

    //the image animation only works for lollipop and higher versions
    private void loadAnimatedImage(Resource<Movie> movieResource) {
        supportPostponeEnterTransition();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestManager.load(movieResource.data.getPosterPath()).into(binding.detailsPosterImage);
            binding.detailsPosterImage.setTransitionName(transitionName);
        }

        //ViewCompat.setTransitionName(binding.detailsPosterImage,C.IMAGE_ANIMATION_KEY);
    }

    //this is to hide the toolbar flash when the image animation starts
    private void hideFadeAndToolbarFlash(){
        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }
    }

}
