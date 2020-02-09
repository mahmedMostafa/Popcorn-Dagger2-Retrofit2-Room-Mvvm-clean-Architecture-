package com.example.popcorn.di;

import android.app.Application;

import com.example.popcorn.di.modules.ActivityBuildersModule;
import com.example.popcorn.di.modules.AppModule;
import com.example.popcorn.di.modules.DbModule;
import com.example.popcorn.di.modules.ViewModelModule;
import com.example.popcorn.util.MyApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuildersModule.class,
                AppModule.class,
                DbModule.class,
                ViewModelModule.class
        }
)
public interface AppComponent extends AndroidInjector<MyApplication> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}


