package com.example.popcorn;

public class Testing {


//    private void createTestCall(){
//        movieApi.getPopularMovies(C.API_KEY,1)
//                .subscribeOn(Schedulers.io())
//                .doOnNext(new Consumer<MoviesApiResponse>() {
//                    @Override
//                    public void accept(MoviesApiResponse moviesApiResponse) throws Exception {
//                        Log.d(TAG, "accept: " + moviesApiResponse.getResults().get(1).getTitle());
//                        adapter.setMoviesList(moviesApiResponse.getResults());
//                        //movies = new ArrayList<>(moviesApiResponse.getResults());
//                    }
//                })
//                .onErrorReturn(new Function<Throwable, MoviesApiResponse>() {
//                    @Override
//                    public MoviesApiResponse apply(Throwable throwable) throws Exception {
//                        Log.d(TAG, "apply: Mohamed " + throwable.getMessage());
//                        return null;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe();
//    }
}
