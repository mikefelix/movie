package com.overstock.android.interview.kotlin

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

  /**
   * See [full docs][https://developers.themoviedb.org/3/search/search-movies].
   */
  @GET("3/search/movie?api_key=8debca31c5ee68298e435848c77b7b5e")
  fun searchMovies(
    @Query("query") query: String,
    @Query("page") pageNr: Int? = null
  ): Single<MovieSearchResponse>

  /**
   * See [full docs][https://developers.themoviedb.org/3/movies/get-popular-movies].
   */
  @GET("3/movie/popular?api_key=8debca31c5ee68298e435848c77b7b5e")
  fun searchPopularMovies(
    @Query("page") pageNr: Int? = null
  ): Single<MovieSearchResponse>

}

data class MovieSearchResponse(
  @SerializedName("page") val page: Int = 0,
  @SerializedName("total_pages") val totalPages: Int = 0,
  @SerializedName("results") val results: List<MovieResult>
)

@Suppress("unused")
data class MovieResult(
  @SerializedName("id") val id: Long = 0,
  @SerializedName("title") val title: String? = null,
  @SerializedName("poster_path") val relativePosterPath: String? = null,
  @SerializedName("release_date") val releaseDate: String? = null,
  @SerializedName("vote_average") val averageRating: Float?,
  @SerializedName("genre_ids") val genreIds: List<Int> = emptyList()
)

@Suppress("unused")
private fun genreIdToGenreName(id: Int): String? {
  return when (id) {
    28 -> "Action"
    12 -> "Adventure"
    16 -> "Animation"
    35 -> "Comedy"
    80 -> "Crime"
    99 -> "Documentary"
    18 -> "Drama"
    10751 -> "Family"
    14 -> "Fantasy"
    36 -> "History"
    27 -> "Horror"
    10402 -> "Music"
    9648 -> "Mystery"
    10749 -> "Romance"
    878 -> "Science Fiction"
    10770 -> "TV Movie"
    53 -> "Thriller"
    10752 -> "War"
    37 -> "Western"
    else -> null
  }
}

