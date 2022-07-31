package com.example.moviesapp.utils

import com.example.moviesapp22.data.Movie
import com.example.moviesapp22.data.Review
import com.example.moviesapp22.data.Trailer
import org.json.JSONObject

class JSONUtils {
    companion object {
        private const val KEY_RESULTS = "results"

        //Для отзывов
        private const val KEY_AUTHOR = "author"
        private const val KEY_CONTENT = "content"

        //Для трейлеров
        private const val KEY_KEY_OF_VIDEO = "key"
        private const val KEY_NAME = "name"
        private const val BASE_URL_YOUTUBE = "https://www.youtube.com/watch?v="

        //Для информации о фильме
        private const val KEY_VOTE_COUNT = "vote_count"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_ORIGINAL_TITLE = "original_title"
        private const val KEY_OVERVIEW = "overview"
        private const val KEY_POSTER_PATH = "poster_path"
        private const val KEY_BACKDROP_PATH = "backdrop_path"
        private const val KEY_VOTE_AVERAGE = "vote_average"
        private const val KEY_RELEASE_DATE = "release_date"

        const val BASE_URL_POSTER = "https://image.tmdb.org/t/p/"
        const val SMALL_POSTER_SIZE = "w185"
        const val BIG_POSTER_SIZE = "w780"

        fun getReviewsFromJSON(jsonObject: JSONObject?) : ArrayList<Review> {
            val result: ArrayList<Review> = ArrayList()
            if (jsonObject == null) {
                return result
            }
            try {
                val jsonArray = jsonObject.getJSONArray(KEY_RESULTS)
                for (i in 0..jsonArray.length()) {
                    val jsonObjectReview = jsonArray.getJSONObject(i)
                    val author: String = jsonObjectReview.getString(KEY_AUTHOR)
                    val content: String = jsonObjectReview.getString(KEY_CONTENT)
                    val review = Review(author = author, content = content)
                    result.add(review)
                }
            } catch (e: Exception) {
            }
            return result
        }

        fun getTrailerFromJSON(jsonObject: JSONObject?) : ArrayList<Trailer> {
            val result: ArrayList<Trailer> = ArrayList()
            if (jsonObject == null) {
                return result
            }
            try {
                val jsonArray = jsonObject.getJSONArray(KEY_RESULTS)
                for (i in 0..jsonArray.length()) {
                    val jsonObjectTrailer = jsonArray.getJSONObject(i)
                    val name: String = jsonObjectTrailer.getString(KEY_NAME)
                    val key: String = BASE_URL_YOUTUBE + jsonObjectTrailer.getString(KEY_KEY_OF_VIDEO)
                    val trailer = Trailer(key = key, name = name)
                    result.add(trailer)
                }
            } catch (e: Exception) {
            }
            return result
        }

        fun getMovieFromJSON(jsonObject: JSONObject?): ArrayList<Movie> {
            val result: ArrayList<Movie> = ArrayList()
            if (jsonObject == null) {
                return result
            }
            try {
                val jsonArray = jsonObject.getJSONArray(KEY_RESULTS)
                for (i in 0..jsonArray.length()) {
                    val jsonMovie = jsonArray.getJSONObject(i)
                    val id: Int = jsonMovie.getInt(KEY_ID)
                    val voteCount: Int = jsonMovie.getInt(KEY_VOTE_COUNT)
                    val title: String = jsonMovie.getString(KEY_TITLE)
                    val originalTitle: String = jsonMovie.getString(KEY_ORIGINAL_TITLE)
                    val overview: String = jsonMovie.getString(KEY_OVERVIEW)
                    val posterPath: String =
                        BASE_URL_POSTER + SMALL_POSTER_SIZE + jsonMovie.getString(KEY_POSTER_PATH)
                    val bigPosterPath: String =
                        BASE_URL_POSTER + BIG_POSTER_SIZE + jsonMovie.getString(KEY_POSTER_PATH)
                    val backdropPath: String = jsonMovie.getString(KEY_BACKDROP_PATH)
                    val voteAverage: Double = jsonMovie.getDouble(KEY_VOTE_AVERAGE)
                    val releaseDate: String = jsonMovie.getString(KEY_RELEASE_DATE)
                    val movie = Movie(
                        id,
                        voteCount,
                        title,
                        originalTitle,
                        overview,
                        posterPath,
                        bigPosterPath,
                        backdropPath,
                        voteAverage,
                        releaseDate
                    )
                    result.add(movie)
                }
            } catch (e: Exception) {

            }

            return result
        }
    }
}