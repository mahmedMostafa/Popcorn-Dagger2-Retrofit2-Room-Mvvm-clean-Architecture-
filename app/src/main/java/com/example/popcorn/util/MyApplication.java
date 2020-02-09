package com.example.popcorn.util;


import com.example.popcorn.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class MyApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return  DaggerAppComponent.builder().application(this).build();
    }

}
