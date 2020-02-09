package com.example.popcorn.persistence.converters;

import androidx.room.TypeConverter;

import com.example.popcorn.models.api.Cast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CastListTypeConverter {

    @TypeConverter
    public static List<Cast> fromString(String value) {
        Type listType = new TypeToken<List<Cast>>() {}.getType();
        List<Cast> casts = new Gson().fromJson(value, listType);
        return casts;
    }

    @TypeConverter
    public static String fromList(List<Cast> list) {
        return new Gson().toJson(list);
    }
}
