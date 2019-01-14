package com.overstock.android.interview.kotlin

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.overstock.android.R

class MoviesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var movies: List<MovieResult> = emptyList()

  override fun getItemCount(): Int = movies.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val movieItemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
    return MovieViewHolder(movieItemView)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as? MovieViewHolder)?.bind(movies[position])
  }

  fun replaceMovies(moviesBeingAdded: List<MovieResult>) {
    val currentMovieCount = movies.size
    if (currentMovieCount > 0) {
      movies = emptyList()
      notifyItemRangeRemoved(0, currentMovieCount)
    }

    if (moviesBeingAdded.isNotEmpty()) {
      movies = moviesBeingAdded
      notifyItemRangeInserted(0, movies.size)
    }
  }
}

class MovieViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
  private val title: TextView by lazy { itemView.findViewById<TextView>(R.id.title) }
  private val details: TextView by lazy { itemView.findViewById<TextView>(R.id.details) }
  private val poster: ImageView by lazy { itemView.findViewById<ImageView>(R.id.poster) }
  private val rating: TextView by lazy { itemView.findViewById<TextView>(R.id.rating)}

  fun bind(movie: MovieResult) {
    title.text = movie.title
    details.text = movie.details()

    if (movie.averageRating == null) {
      rating.visibility = View.GONE
    }
    else {
      movie.averageRating.let {
        rating.text = it.toString()
        rating.setBackgroundResource(when {
          it < 4 -> R.drawable.rating_red
          it >= 7 -> R.drawable.rating_green
          else -> R.drawable.rating_yellow
        })
      }
    }

    Glide.with(itemView.context)
      .load("http://image.tmdb.org/t/p/$WIDTH_OPTION/${movie.relativePosterPath}")
      .apply(RequestOptions().error(R.drawable.no_poster_image).override(Target.SIZE_ORIGINAL))
      .into(poster)
  }
}

private const val WIDTH_OPTION = "w500" // all available options: w92, w154, w185, w342, w500, w780 or original
