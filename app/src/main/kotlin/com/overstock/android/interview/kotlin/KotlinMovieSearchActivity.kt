package com.overstock.android.interview.kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.overstock.android.R
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io

class KotlinMovieSearchActivity : AppCompatActivity() {
  private val adapter: MoviesAdapter = MoviesAdapter()
  private val movieApi: MovieApi by lazy { (application as KotlinApp).movieApi }
  private val searchHandler = Handler()

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

    searchField.addTextChangedListener(object: TextWatcher {
      private var searchQuery = ""

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val text = s?.toString()?.trim() ?: ""
        if (searchQuery != text){
          searchQuery = text
          searchHandler.postDelayed({
            if (searchQuery == text){
              search(searchQuery)
            }
          },800)
        }
      }

      override fun afterTextChanged(s: Editable?) = Unit
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    })

    search()
  }

  @SuppressLint("CheckResult")
  private fun search(query: String? = null) {
    adapter.beginLoad()

    val source = if (query?.isNotBlank() == true) movieApi.searchMovies(query) else movieApi.searchPopularMovies()

    source
      .subscribeOn(io())
      .observeOn(mainThread())
      .subscribe { response, error ->
        error?.printStackTrace()
        adapter.replaceMovies(if (error == null) response.results else emptyList())
      }
  }

}
