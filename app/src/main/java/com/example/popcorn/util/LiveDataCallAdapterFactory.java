package com.example.popcorn.util;

import androidx.lifecycle.LiveData;

import com.example.popcorn.remote.ApiResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/*
    if you don't remember this class just copy and paste it , it doesn't change at all
 */
public class LiveDataCallAdapterFactory extends CallAdapter.Factory {


    /*
        this method is gonna perform a number of checks and then returns the Response type for the retrofit requests
        (@bodyType is the ResponseType it can be for instance MoviesApiResponse or anything else)
     */
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        //CHECK 1)
        //Make sure that the callAdapter is returning a type of livedata
        if (CallAdapter.Factory.getRawType(returnType) != LiveData.class) {
            return null;
        }

        //CHECK 2)
        //Check for the type of the livedata returning in our case it's ApiResponse -----> LiveData<ApiResponse>>
        Type observableType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) returnType);

        //Check if it's really ApiResponse
        Type rawObservableType = CallAdapter.Factory.getRawType(observableType);
        if (rawObservableType != ApiResponse.class) {
            throw new IllegalArgumentException("Type must be a defined resource");
        }

        //CHECK 3)
        //Check if ApiResponse is parameterized AKA (Has something inside it <>)
        //in our case it's MoviesApiResponse
        if (!(observableType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("resource must be parametrized");
        }

        //Finally get the body type
        Type bodyType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) observableType);

        return new LiveDataCallAdapter<Type>(bodyType);
    }
}














