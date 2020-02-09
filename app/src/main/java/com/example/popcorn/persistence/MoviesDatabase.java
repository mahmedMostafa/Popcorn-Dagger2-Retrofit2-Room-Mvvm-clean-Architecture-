package com.example.popcorn.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.popcorn.models.api.Movie;
import com.example.popcorn.persistence.converters.CastListTypeConverter;
import com.example.popcorn.persistence.converters.GenerListTypeConverter;

import static com.example.popcorn.util.C.DATABASE_NAME;

@Database(entities = Movie.class , version = 1)
@TypeConverters({GenerListTypeConverter.class, CastListTypeConverter.class})
public abstract class MoviesDatabase extends RoomDatabase {

    private static MoviesDatabase instance;

    public abstract MoviesDao getMoviesDao();

    public static synchronized MoviesDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MoviesDatabase.class,
                    DATABASE_NAME)
                    .build();
        }
        return instance;
    }


}
