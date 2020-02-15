package com.example.popcorn.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.popcorn.R;
import com.example.popcorn.adapters.MoviesRecyclerAdapter;
import com.example.popcorn.adapters.OnItemClickListener;
import com.example.popcorn.databinding.ActivityMainBinding;
import com.example.popcorn.factory.ViewModelProviderFactory;
import com.example.popcorn.models.api.Movie;
import com.example.popcorn.remote.Resource;
import com.example.popcorn.ui.details.DetailsActivity;
import com.example.popcorn.util.C;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.example.popcorn.util.C.QUERY_EXHAUSTED;

public class MainActivity extends DaggerAppCompatActivity implements OnItemClickListener {

    private static final String TAG = "MainActivity";

    private List<Movie> movies;
    private MoviesRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory factory;

    @Inject
    RequestManager requestManager;

    @Inject
    ViewPreloadSizeProvider preloadSizeProvider;

    private MoviesViewModel viewModel;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        hideFadeAndToolbarFlash();
        setupRecyclerView();
        viewModel = ViewModelProviders.of(this, factory).get(MoviesViewModel.class);
        if(savedInstanceState == null)
            viewModel.searchMovies(1);
        subscribeObservers();
    }

    private void setupRecyclerView() {
        movies = new ArrayList<>();
        adapter = new MoviesRecyclerAdapter(movies, this, requestManager, preloadSizeProvider);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //glide preloader
        RecyclerViewPreloader<String> preloader = new RecyclerViewPreloader<String>(
                requestManager,
                adapter,
                preloadSizeProvider,
                30
        );
        binding.recyclerView.addOnScrollListener(preloader);
        //pagination
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!binding.recyclerView.canScrollVertically(1)) {

                    viewModel.searchNextPage();
                }
            }
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void subscribeObservers() {
        //binding.recyclerView.smoothScrollToPosition(0);

        viewModel.getMovies().observe(this, new Observer<Resource<List<Movie>>>() {
            @Override
            public void onChanged(Resource<List<Movie>> listResource) {
                Log.d(TAG, "onChanged: STATUS is :" + listResource.status);
                if (listResource.data != null) {
                    switch (listResource.status) {
                        case SUCCESS: {
                            Log.d(TAG, "onChanged: Cache has been refreshed");
                            adapter.hideLoading();
                            Log.d(TAG, "onChanged: Movie is " + listResource.data.get(0).toString());
                            adapter.setMoviesList(listResource.data);
                            movies = listResource.data;
                            break;
                        }
                        case LOADING: {
                            if (viewModel.getPageNumber() > 1) {
                                adapter.displayPaginationLoading();
                            } else {
                                adapter.displayOnlyLoading();
                            }
                            break;
                        }
                        case ERROR: {
                            Log.d(TAG, "onChanged: can't refresh the cache");
                            Log.e(TAG, "onChanged: Error message is :" + listResource.message);
                            adapter.hideLoading();
                            //set cached data
                            Toast.makeText(MainActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                            adapter.setMoviesList(listResource.data);
                            movies = listResource.data;
                            if (listResource.message.equals(QUERY_EXHAUSTED)) {
                                adapter.setQueryExhausted();
                            }
                            break;
                        }
                    }
                }
            }
        });

        Log.d(TAG, "onItemClick: list size is BEFORE: " + movies.size());
    }

    @Override
    public void onBackPressed() {
        viewModel.cancelSearchRequest();
        super.onBackPressed();
    }

    @Override
    public void onItemClick(int position, ImageView image) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(C.MOVIE_ID_KEY, String.valueOf(movies.get(position).getId()));
        intent.putExtra(C.IMAGE_ANIMATION_KEY, ViewCompat.getTransitionName(image));
        //Image Animation
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                MainActivity.this, image, ViewCompat.getTransitionName(image)
        );
        startActivity(intent, optionsCompat.toBundle());
    }

    //this is to hide the toolbar flash when the image animation starts
    private void hideFadeAndToolbarFlash() {
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
