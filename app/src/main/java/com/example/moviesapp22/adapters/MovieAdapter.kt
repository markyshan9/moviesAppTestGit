package com.example.moviesapp22.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp22.R
import com.example.moviesapp22.data.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private var movies : MutableList<Movie> = mutableListOf()
    private var onPosterClickListener: OnPosterClickListener? = null
    private var onReachEndListener: OnReachEndListener? = null



    interface OnPosterClickListener{
        fun onPosterClick(position: Int)
    }

    interface OnReachEndListener{
        fun onReachEnd()
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val imageViewSmallPoster: ImageView = itemView.findViewById(R.id.imageViewSmallPoster)
        init {
            itemView.setOnClickListener(View.OnClickListener() { onClick(itemView) })
        }

        override fun onClick(p0: View?) {
            if(onPosterClickListener != null) {
                onPosterClickListener?.onPosterClick(adapterPosition)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if (movies.size >= 20 && position>(movies.size -4) && onReachEndListener != null) {
            onReachEndListener?.onReachEnd()
        }
        val movie: Movie = movies.get(index = position)
        Picasso.get().load(movie.posterPath).into(holder.imageViewSmallPoster)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun clear() {
        this.movies.clear()
        notifyDataSetChanged()
    }
    fun addMovies(movies: MutableList<Movie>) {
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    fun setMovies(movies: MutableList<Movie>) {
        if (movies != null) {
            this.movies = movies
        }
        notifyDataSetChanged()
    }

    fun getMovies() :MutableList<Movie>{
        return movies
    }

    fun setOnPosterClickListener(onPosterClickListener: OnPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener
    }

    fun setOnReachEndListener(onReachEndListener: OnReachEndListener) {
        this.onReachEndListener = onReachEndListener
    }
}