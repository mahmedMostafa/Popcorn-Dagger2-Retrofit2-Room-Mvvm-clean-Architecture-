package com.example.popcorn.di.modules;

import android.app.Application;

import androidx.room.Room;

import com.example.popcorn.persistence.MoviesDao;
import com.example.popcorn.persistence.MoviesDatabase;
import com.example.popcorn.util.C;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Singleton
    @Provides
    static MoviesDatabase provideDatabase(Application application){
        return Room.databaseBuilder(
                application,
                MoviesDatabase.class,
                C.DATABASE_NAME
        ).build();
    }

    @Singleton
    @Provides
    static MoviesDao provideMoviesDao(MoviesDatabase database){
        return database.getMoviesDao();
    }
}
