package com.example.popcorn.util;

public class C {

    //For Api
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "f10294cfb88656e9d26c1db1b34c8a7d";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500%s";

    public static final int CONNECTION_TIMEOUT = 10; // 10 seconds
    public static final int READ_TIMEOUT = 2; // 2 seconds
    public static final int WRITE_TIMEOUT = 2; // 2 seconds

    public static final int MOVIE_REFRESH_TIME = 60 * 60 * 24 * 10;

    public static final String MOVIE_ID_KEY = "movie_id";

    public static final String DATABASE_NAME = "movies_db";

    public static final String QUERY_EXHAUSTED = "no more results";

    public static final String IMAGE_ANIMATION_KEY = "transaction";

}
