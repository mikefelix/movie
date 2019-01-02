package com.overstock.android.interview.kotlin

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.overstock.android.R
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io


class KotlinMovieSearchActivity : AppCompatActivity() {
  private val adapter: MoviesAdapter = MoviesAdapter()
  private val movieApi: MovieApi by lazy { (application as KotlinApp).movieApi }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_search)

    val moviesRecycler = findViewById<RecyclerView>(R.id.movies_recycler)
    moviesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    moviesRecycler.adapter = adapter

    val searchField = findViewById<EditText>(R.id.search_field)
    searchField.setOnEditorActionListener { v, actionId, event ->
      val ims = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
      ims?.hideSoftInputFromWindow(searchField.windowToken, 0)
      search(searchField.text.toString())
      true
    }

    search()
  }

  private fun search(query: String? = null) {
    val source =
      if (query?.isNotBlank() == true) movieApi.searchMovies(query) else movieApi.searchPopularMovies()

    source
      .subscribeOn(io())
      .observeOn(mainThread())
      .subscribe { response, error ->
        error?.printStackTrace()
        adapter.replaceMovies(if (error == null) response.results else emptyList())
      }
  }

}
