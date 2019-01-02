package com.overstock.android.interview.java.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResult {
  @SerializedName("id")
  Long id;

  @SerializedName("title")
  String title;

  @SerializedName("poster_path")
  String relativePosterPath;

  @SerializedName("release_date")
  String releaseDate;

  @SerializedName("vote_average")
  Float averageRating;

  @SerializedName("genre_ids")
  List genreIds;

  public MovieResult() {
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getRelativePosterPath() {
    return relativePosterPath;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public Float getAverageRating() {
    return averageRating;
  }

  public List getGenreIds() {
    return genreIds;
  }

  public static String genreIdToGenreName(int genreId) {
    switch (genreId) {
      case 28:
        return "Action";
      case 12:
        return "Adventure";
      case 16:
        return "Animation";
      case 35:
        return "Comedy";
      case 80:
        return "Crime";
      case 99:
        return "Documentary";
      case 18:
        return "Drama";
      case 10751:
        return "Family";
      case 14:
        return "Fantasy";
      case 36:
        return "History";
      case 27:
        return "Horror";
      case 10402:
        return "Music";
      case 9648:
        return "Mystery";
      case 10749:
        return "Romance";
      case 878:
        return "Science Fiction";
      case 10770:
        return "TV Movie";
      case 53:
        return "Thriller";
      case 10752:
        return "War";
      case 37:
        return "Western";
      default:
        return null;
    }
  }
}
