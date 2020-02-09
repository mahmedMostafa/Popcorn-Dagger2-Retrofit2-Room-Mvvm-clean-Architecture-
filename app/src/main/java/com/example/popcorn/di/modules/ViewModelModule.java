package com.example.popcorn.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.popcorn.di.ViewModelKey;
import com.example.popcorn.factory.ViewModelProviderFactory;
import com.example.popcorn.ui.details.DetailsViewModel;
import com.example.popcorn.ui.main.MoviesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel.class)
    public abstract ViewModel bindMainViewModel(MoviesViewModel moviesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel.class)
    public abstract ViewModel bindDetailsViewModel(DetailsViewModel detailsViewModel);

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelFactory);
}
