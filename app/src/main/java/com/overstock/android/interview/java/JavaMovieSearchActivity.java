package com.overstock.android.interview.java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.overstock.android.R;
import com.overstock.android.interview.java.api.MovieApi;
import com.overstock.android.interview.java.api.MovieSearchResponse;
import io.reactivex.Single;

import java.util.Collections;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;

public class JavaMovieSearchActivity extends AppCompatActivity {
  private final MoviesAdapter adapter = new MoviesAdapter();
  private MovieApi movieApi;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    movieApi = ((JavaApp) getApplication()).getMovieApi();

    setContentView(R.layout.activity_movie_search);

    RecyclerView moviesRecycler = findViewById(R.id.movies_recycler);
    moviesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    moviesRecycler.setAdapter(adapter);

    EditText searchField = findViewById(R.id.search_field);
    searchField.setOnEditorActionListener((v, actionId, event) -> {
      hideKeyboard();
      search(searchField.getText().toString());
      return true;
    });

    search(null);
  }

  public void hideKeyboard() {
    View view = getCurrentFocus();
    if (view == null) {
      view = findViewById(android.R.id.content);
    }
    if (view == null) {
      return;
    }

    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  @SuppressLint("CheckResult")
  private void search(@Nullable String query) {
    boolean isEmptyQuery = query == null || query.trim().isEmpty();
    Single<MovieSearchResponse> source = isEmptyQuery ? movieApi.searchPopularMovies(null) : movieApi.searchMovies(query, null);

    source.subscribeOn(io()).observeOn(mainThread()).subscribe((response, error) -> {
      if (error != null) {
        error.printStackTrace();
        adapter.replaceMovies(Collections.emptyList());
      }
      else {
        adapter.replaceMovies(response.getResults());
      }
    });
  }
}
