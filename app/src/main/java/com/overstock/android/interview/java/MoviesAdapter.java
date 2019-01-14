package com.overstock.android.interview.java;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.overstock.android.R;
import com.overstock.android.interview.java.api.MovieResult;

import java.util.List;

import static java.util.Collections.emptyList;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<MovieResult> movies = emptyList();

  @Override
  public int getItemCount() {
    return movies.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View movieItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
    return new MovieViewHolder(movieItemView);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ((MovieViewHolder) holder).bind(movies.get(position));
  }

  public void replaceMovies(List<MovieResult> moviesBeingAdded) {
    int currentMovieCount = movies.size();
    if (currentMovieCount > 0) {
      movies = emptyList();
      notifyItemRangeRemoved(0, currentMovieCount);
    }

    if (!moviesBeingAdded.isEmpty()) {
      movies = moviesBeingAdded;
      notifyItemRangeInserted(0, movies.size());
    }
  }

  private static class MovieViewHolder extends RecyclerView.ViewHolder {
    private final TextView title = itemView.findViewById(R.id.title);
    private final ImageView poster = itemView.findViewById(R.id.poster);

    MovieViewHolder(View itemView) {
      super(itemView);
    }

    void bind(MovieResult movie) {
      title.setText(movie.getTitle());

      Glide
        .with(itemView.getContext())
        .load("http://image.tmdb.org/t/p/" + WIDTH_OPTION + "/" + movie.getRelativePosterPath())
        .apply(new RequestOptions().error(R.drawable.no_poster_image).override(Target.SIZE_ORIGINAL))
        .into(poster);
    }

    private static final String WIDTH_OPTION = "w500";
      // all available options: w92, w154, w185, w342, w500, w780 or original
  }
}
