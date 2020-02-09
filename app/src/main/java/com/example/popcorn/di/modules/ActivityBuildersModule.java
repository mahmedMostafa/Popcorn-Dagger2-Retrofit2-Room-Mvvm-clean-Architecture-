package com.example.popcorn.di.modules;

import com.example.popcorn.ui.details.DetailsActivity;
import com.example.popcorn.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector()
    abstract DetailsActivity contributeDetailsActivity();
}
