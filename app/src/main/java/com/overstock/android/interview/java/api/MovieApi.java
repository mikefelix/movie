package com.overstock.android.interview.java.api;

import android.support.annotation.Nullable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

  /**
   * See [full docs][https://developers.themoviedb.org/3/search/search-movies].
   */
  @GET("3/search/movie?api_key=8debca31c5ee68298e435848c77b7b5e")
  Single<MovieSearchResponse> searchMovies(@Query("query") String query, @Query("page") @Nullable Integer pageNr);

  /**
   * See [full docs][https://developers.themoviedb.org/3/movies/get-popular-movies].
   */
  @GET("3/movie/popular?api_key=8debca31c5ee68298e435848c77b7b5e")
  Single<MovieSearchResponse> searchPopularMovies(@Query("page") @Nullable Integer pageNr);

}

