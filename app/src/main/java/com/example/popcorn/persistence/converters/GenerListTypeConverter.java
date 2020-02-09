package com.example.popcorn.persistence.converters;

import androidx.room.TypeConverter;

import com.example.popcorn.models.api.Gener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GenerListTypeConverter {


    @TypeConverter
    public static List<Gener> fromString(String value){
        Type listType = new TypeToken<List<Gener>>(){}.getType();
        List<Gener> geners = new Gson().fromJson(value,listType);
        return geners;
    }

    @TypeConverter
    public static String fromList(List<Gener> geners){
        return new Gson().toJson(geners);
    }
}
