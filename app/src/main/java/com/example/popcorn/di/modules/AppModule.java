package com.example.popcorn.di.modules;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.example.popcorn.R;
import com.example.popcorn.persistence.MoviesDao;
import com.example.popcorn.remote.api.MovieApi;
import com.example.popcorn.remote.interceptor.RequestInterceptor;
import com.example.popcorn.repositories.MovieRepository;
import com.example.popcorn.util.C;
import com.example.popcorn.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    //TODO later on scope this dependency if you add more fragments or something
    @Singleton
    @Provides
    static MovieApi provideMovieApi(Retrofit retrofit){
        return retrofit.create(MovieApi.class);
    }

    @Singleton
    @Provides
    static Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(C.BASE_URL)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    static RequestInterceptor provideRequestInterceptor(){
        return new RequestInterceptor();
    }

    @Singleton
    @Provides
    static OkHttpClient provideOkhttpClient(RequestInterceptor requestInterceptor){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(C.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(C.READ_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(C.WRITE_TIMEOUT,TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(requestInterceptor)
                .build();
        return client;
    }

    @Singleton
    @Provides
    static MovieRepository provideMovieRepository(MoviesDao moviesDao,MovieApi movieApi){
        return new MovieRepository(moviesDao,movieApi);
    }

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions() {
        return RequestOptions
                .placeholderOf(R.drawable.white_background)
                .error(R.drawable.white_background);
    }

    @Singleton
    @Provides
    static RequestManager provideRequestManager(Application application, RequestOptions requestOptions) {
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }

    @Singleton
    @Provides
    static ViewPreloadSizeProvider preloadSizeProvider(){
        return new ViewPreloadSizeProvider();
    }

}
