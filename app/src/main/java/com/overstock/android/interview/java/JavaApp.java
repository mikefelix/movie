package com.overstock.android.interview.java;

import android.app.Application;
import com.overstock.android.interview.java.api.MovieApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class JavaApp extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Timber.plant(new Timber.DebugTree());
  }

  private OkHttpClient httpClient;
  private Retrofit retrofit;
  private MovieApi movieApi;

  public OkHttpClient getHttpClient() {
    if (httpClient == null) {
      httpClient = new OkHttpClient().newBuilder().addInterceptor(chain -> {
        Timber.i("Request: %s", chain.request().url());
        return chain.proceed(chain.request());
      }).build();
    }
    return httpClient;
  }

  public Retrofit getRetrofit() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .client(getHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
    }
    return retrofit;
  }

  public MovieApi getMovieApi() {
    if (movieApi == null) {
      movieApi = getRetrofit().create(MovieApi.class);
    }
    return movieApi;
  }
}