package com.example.popcorn.remote;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private static final String TAG = "NetworkBoundResource";

    private MediatorLiveData<Resource<CacheObject>> results = new MediatorLiveData<>();

    public NetworkBoundResource() {
        //TODO use executors if rxJava didn't work our
        init();
    }

    private void init() {

        //assigning a loading status for the data
        results.setValue((Resource<CacheObject>) Resource.loading(null));

        //load the data from the local db
        final LiveData<CacheObject> dbSource = loadFromDb();

        //add a source to see if there is any data in the cache or not
        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                //remove the source so it stops listening as we wanted to trigger tha onChange method only
                results.removeSource(dbSource);

                if (shouldFetch(cacheObject)) {
                    fetchFromNetwork(dbSource);
                } else {
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(CacheObject cacheObject) {
                            setNewValue(Resource.success(cacheObject));
                        }
                    });
                }
            }
        });

    }

    /*
        this is to insert new data into the local db and begin observing the refreshed data again
     */
    private void fetchFromNetwork(final LiveData<CacheObject> dbSource) {
        Log.d(TAG, "fetchFromNetwork: called");

        //update live data for loading status
        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                setNewValue(Resource.loading(cacheObject));
            }
        });

        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();

        results.addSource(apiResponse, new Observer<ApiResponse<RequestObject>>() {
            @SuppressLint("CheckResult")
            @Override
            public void onChanged(final ApiResponse<RequestObject> requestObjectApiResponse) {
                results.removeSource(dbSource);
                results.removeSource(apiResponse);

                if (requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse) {
                    Log.d(TAG, "onChanged: ApiSuccessResponse");
                    //so we basically save the call request on a background thread and then we post the new data on the main thread
                    Observable.create(new ObservableOnSubscribe<Object>() {
                        @Override
                        public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                            {
                                saveCallResult((RequestObject) processResponse((ApiResponse.ApiSuccessResponse)requestObjectApiResponse));

                                // we specially request a new live data,
                                // otherwise we will get immediately last cached value,
                                // which may not be updated with latest results received from network.
                                Observable.create(new ObservableOnSubscribe<Object>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                        results.addSource(loadFromDb(), new Observer<CacheObject>() {
                                            @Override
                                            public void onChanged(CacheObject cacheObject) {
                                                setNewValue(Resource.success(cacheObject));
                                            }
                                        });
                                    }
                                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
                            }
                        }
                    }).subscribeOn(Schedulers.io()).subscribe();
                } else if (requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse) {
                    Log.d(TAG, "onChanged: ApiEmptyResponse");
                    Observable.create(new ObservableOnSubscribe<Object>() {
                        @Override
                        public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                            results.addSource(loadFromDb(), new Observer<CacheObject>() {
                                @Override
                                public void onChanged(CacheObject cacheObject) {
                                    setNewValue(Resource.success(cacheObject));
                                }
                            });
                        }
                    }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
                } else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse) {
                    Log.d(TAG, "onChanged: ApiErrorResponse");
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(CacheObject cacheObject) {
                            setNewValue(
                                    Resource.error(
                                            ((ApiResponse.ApiErrorResponse)requestObjectApiResponse).getErrorMessage(),
                                            cacheObject
                                    )
                            );
                        }
                    });
                }
            }
        });
    }

    private CacheObject processResponse(ApiResponse.ApiSuccessResponse response){
        return (CacheObject) response.getBody();
    }

    private void setNewValue(Resource<CacheObject> cacheObjectResource) {
        if (results.getValue() != cacheObjectResource) {
            results.setValue(cacheObjectResource);
        }
    }


    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(CacheObject cacheObject);

    // Called to get the cached data from the database.
    @NonNull
    @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    // Returns a livedata object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<CacheObject>> getAsLiveData() {
        return results;
    }

    ;
}
