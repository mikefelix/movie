package com.overstock.android.interview.kotlin

import android.app.Application
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class KotlinApp : Application() {
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
  }

  private val httpClient: OkHttpClient by lazy {
    OkHttpClient().newBuilder()
      .addInterceptor { chain ->
        Timber.i("Request: %s", chain.request().url())
        chain.proceed(chain.request())
      }
      .build()
  }

  private val retrofit: Retrofit by lazy {
    Retrofit.Builder()
      .client(httpClient)
      .baseUrl("https://api.themoviedb.org/")
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()
  }

  val movieApi: MovieApi by lazy {
    retrofit.create(MovieApi::class.java)
  }

}