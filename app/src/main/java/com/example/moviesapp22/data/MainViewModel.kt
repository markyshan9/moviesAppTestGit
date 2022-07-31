package com.example.moviesapp22.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {
    init {
        database = MovieDatabase.getInstance(getApplication())
        movies = database.movieDao().getAllMovies()
        favouriteMovies = database.movieDao().getAllFavouriteMovies()
    }

    companion object {
        lateinit var database: MovieDatabase
        lateinit var movies: LiveData<MutableList<Movie>>
        lateinit var favouriteMovies: LiveData<MutableList<FavouriteMovie>>

    }


    fun getMovieById(id: Int): Movie {
        return GetMovieTask().execute(id).get()
    }

    fun deleteAllMovies() {
        DeleteMoviesTask().execute()
    }

    fun insertMovie(movie: Movie) {
        InsertMovieTask().execute(movie)
    }

    fun deleteMovie(movie: Movie) {
        DeleteMovieTask().execute(movie)
    }

    fun getFavouriteMovieById(id: Int): FavouriteMovie? {
        return GetFavouriteMovieTask().execute(id).get()
    }

    fun insertFavouriteMovie(movie: FavouriteMovie) {
        InsertFavouriteMovieTask().execute(movie)
    }

    fun deleteFavouriteMovie(movie: FavouriteMovie) {
        DeleteFavouriteMovieTask().execute(movie)
    }

    fun getMovies(): LiveData<MutableList<Movie>> {
        return movies
    }

    fun getFavouriteMovies(): LiveData<MutableList<FavouriteMovie>> {
        return favouriteMovies
    }


    class DeleteFavouriteMovieTask : AsyncTask<FavouriteMovie, Void, Void>() {
        override fun doInBackground(vararg p0: FavouriteMovie?): Void? {
            if (p0 != null && p0.isNotEmpty()) {
                p0[0].let {
                    if (it != null) {
                        database.movieDao().deleteFavouriteMovie(it)
                    }
                }
            }
            return null
        }
    }

    class InsertFavouriteMovieTask : AsyncTask<FavouriteMovie, Void, Void>() {
        override fun doInBackground(vararg p0: FavouriteMovie?): Void? {
            if (p0 != null && p0.isNotEmpty()) {
                database.movieDao().insertFavouriteMovie(p0[0]!!)
            }
            return null
        }
    }

    class GetFavouriteMovieTask : AsyncTask<Int, Void, FavouriteMovie>() {
        override fun doInBackground(vararg p0: Int?): FavouriteMovie? {
            if (p0 != null && p0.isNotEmpty()) {
                return database.movieDao().getFavouriteMovieById(p0[0]!!)
            }
            return null
        }
    }

    class DeleteMovieTask : AsyncTask<Movie, Void, Void>() {
        override fun doInBackground(vararg p0: Movie?): Void? {
            if (p0 != null && p0.isNotEmpty()) {
                p0[0].let {
                    if (it != null) {
                        database.movieDao().deleteMovie(it)
                    }
                }
            }
            return null
        }
    }

    class InsertMovieTask : AsyncTask<Movie, Void, Void>() {
        override fun doInBackground(vararg p0: Movie?): Void? {
            if (p0 != null && p0.isNotEmpty()) {
                database.movieDao().insertMovie(p0[0]!!)
            }
            return null
        }
    }


    class GetMovieTask : AsyncTask<Int, Void, Movie>() {
        override fun doInBackground(vararg p0: Int?): Movie? {
            if (p0 != null && p0.isNotEmpty()) {
                return p0[0].let { database.movieDao().getMovieById(it!!) }
            }
            return null
        }
    }

    class DeleteMoviesTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void): Void? {
            database.movieDao().deleteAllMovies()
            return null
        }


    }
}