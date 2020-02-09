package com.example.popcorn.remote;

import android.util.Log;

import java.io.IOException;

import retrofit2.Response;

//this is class is gonna handle all the possible responses from the network requests
public class ApiResponse<T> {

    private static final String TAG = "ApiResponse";

    public ApiResponse<T> create(Throwable error){
        return new ApiErrorResponse<>(error.getMessage().equals("") ? error.getMessage() : "Unknown error \n Check Network Connection");
    }

    public ApiResponse<T> create(Response<T> response){

        if(response.isSuccessful()){
            T body = response.body();

            //204 is empty response
            if(body == null || response.code() == 204){
                return new ApiEmptyResponse<>();
            }else{
                return new ApiSuccessResponse<>(body);
            }
        }else{
            String errorMessage = "";

            try{
                errorMessage = response.errorBody().string();
            }catch (IOException e){
                e.printStackTrace();
                Log.e(TAG, "create: ERROR IS : "+e.getMessage());
                errorMessage = e.getMessage();
            }
            return new ApiErrorResponse<>(errorMessage);
        }
    }

    public class ApiSuccessResponse<T> extends ApiResponse<T>{

        private T body;

        public ApiSuccessResponse(T body){
            this.body = body;
        }

        public T getBody(){
            return body;
        }
    }

    public class ApiErrorResponse<T> extends ApiResponse<T>{

        private String errorMessage;

        public ApiErrorResponse(String errorMessage){
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage(){
            return errorMessage;
        }
    }

    public class ApiEmptyResponse<T> extends ApiResponse<T>{}
}
