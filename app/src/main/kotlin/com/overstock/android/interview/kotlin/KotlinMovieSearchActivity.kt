package com.overstock.android.interview.kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.overstock.android.R
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import java.lang.RuntimeException

class KotlinMovieSearchActivity : AppCompatActivity() {
  private val adapter: MoviesAdapter = MoviesAdapter()
  private val movieApi: MovieApi by lazy { (application as KotlinApp).movieApi }
  private val searchHandler = Handler()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_search)

    findViewById<RecyclerView>(R.id.movies_recycler).let {
      it.adapter = this.adapter
      it.layoutManager = when (getResources().getConfiguration().orientation) {
        Configuration.ORIENTATION_PORTRAIT -> GridLayoutManager(this, 2)
        Configuration.ORIENTATION_LANDSCAPE -> LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        else -> throw RuntimeException("Unknown orientation")
      }
    }

    findViewById<EditText>(R.id.search_field).apply {
      setOnEditorActionListener { v, actionId, event ->
        val ims = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        ims?.hideSoftInputFromWindow(windowToken, 0)
        search(text.toString())
        true
      }

      addTextChangedListener(object: TextWatcher {
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

    }

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
