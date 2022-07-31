package com.example.moviesapp22.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<MutableList<Movie>>

    @Query("DELETE FROM movies")
    fun deleteAllMovies()

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieById(movieId: Int): Movie

    @Insert
    fun insertMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)


    @Query("SELECT * FROM favourite_movies")
    fun getAllFavouriteMovies(): LiveData<MutableList<FavouriteMovie>>

    @Query("SELECT * FROM favourite_movies WHERE id = :movieId")
    fun getFavouriteMovieById(movieId: Int?): FavouriteMovie

    @Insert
    fun insertFavouriteMovie(movie: FavouriteMovie)

    @Delete
    fun deleteFavouriteMovie(movie: FavouriteMovie)
}