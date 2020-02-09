package com.example.popcorn.remote.interceptor;

import com.example.popcorn.util.C;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    /**
     * Observes, modifies, and potentially short-circuits requests going out and the corresponding
     * responses coming back in. Typically interceptors add, remove, or transform headers on the request
     * or response.
     *
     * Here we are adding the api key as a parameter before sending to the server
     */

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl originalUrl = originalRequest.url();
        HttpUrl newUrl = originalUrl.newBuilder()
                .addQueryParameter("api_key", C.API_KEY)
                .build();
        Request.Builder builder = originalRequest.newBuilder().url(newUrl);
        Request request = builder.build();
        return chain.proceed(request);
    }

}
