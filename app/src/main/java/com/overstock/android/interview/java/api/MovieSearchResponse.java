package com.overstock.android.interview.java.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieSearchResponse {

  @SerializedName("page")
  int page;

  @SerializedName("total_pages")
  int totalPages;

  @SerializedName("results")
  List<MovieResult> results;

  public MovieSearchResponse() {
  }

  public int getPage() {
    return page;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public List<MovieResult> getResults() {
    return results;
  }
}
