package com.example.moviesapp22.utils

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import androidx.loader.content.AsyncTaskLoader
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class NetworkUtils {

    companion object {
        private const val API_KEY = "e09b30b3e5dd475fec3a69c907ff2f99"
        private const val BASE_URL = "https://api.themoviedb.org/3/discover/movie"
        private const val BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos"
        private const val BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews"

        private const val PARAMS_API_KEY = "api_key"
        private const val PARAMS_LANGUAGE = "language"
        private const val PARAMS_SORT_BY = "sort_by"
        private const val PARAMS_PAGE = "page"
        private const val PARAMS_MIN_VOTE_COUNT = "vote_count.gte"

       // private const val LANGUAGE_VALUE = "ru-RU"
        private const val SORT_BY_POPULARITY = "popularity.desc"
        private const val SORT_BY_TOP_RATED = "vote_average.desc"
        private const val MIN_VOTE_COUNT_VALUE = "1000"

        const val POPULARITY: Int = 0
        const val TOP_RATED: Int = 1

        fun buildURL(sortBy: Int, page: Int, lang: String): URL? {
            var result: URL? = null
            val methodOfSort: String = if (sortBy == POPULARITY) {
                SORT_BY_POPULARITY
            } else {
                SORT_BY_TOP_RATED
            }
            val uri: Uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang)
                .appendQueryParameter(PARAMS_SORT_BY, methodOfSort)
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT_VALUE)
                .appendQueryParameter(PARAMS_PAGE, page.toString())
                .build()
            try {
                result = URL(uri.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
            return result
        }

        fun getJSONFromNetwork(sortBy: Int, page: Int, lang: String): JSONObject {
            var result: JSONObject?
            val url = buildURL(sortBy = sortBy, page = page, lang = lang)
            result = JSONLoadTask().execute(url).get()
            return result
        }

        fun buildURLToVideos(id: Int, lang: String) : URL {
            val uri: Uri = Uri.parse(String.format(BASE_URL_VIDEOS, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang)
                .build()
            return URL(uri.toString())
        }

        fun getJSONForVideos(id: Int, lang: String): JSONObject {
            val result: JSONObject
            val url = buildURLToVideos(id = id, lang = lang)
            result = JSONLoadTask().execute(url).get()
            return result
        }

        fun buildURLToReviews(id: Int, lang: String) : URL {
            val uri: Uri = Uri.parse(String.format(BASE_URL_REVIEWS, id)).buildUpon()
                .appendQueryParameter(PARAMS_LANGUAGE, lang)
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .build()
            return URL(uri.toString())
        }

        fun getJSONForReviews(id: Int, lang: String): JSONObject {
            val result: JSONObject
            val url = buildURLToReviews(id = id, lang = lang)
            result = JSONLoadTask().execute(url).get()
            return result
        }

        class JSONLoader(context: Context, private val bundle: Bundle?) : AsyncTaskLoader<JSONObject>(context) {
            private var onStartLoadingListener : OnStartLoadingListener? = null



            interface OnStartLoadingListener {
                fun onStartLoading()
            }

            fun setOnStartLoadingListener(onStartLoadingListener: OnStartLoadingListener) {
                this.onStartLoadingListener = onStartLoadingListener
            }

            override fun onStartLoading() {
                super.onStartLoading()
                if(onStartLoadingListener!=null) {
                    onStartLoadingListener!!.onStartLoading()
                }
                forceLoad()
            }

            override fun loadInBackground(): JSONObject? {
                if(bundle == null) {
                    return null
                }
                val urlAsString : String = bundle!!.getString("url").toString()
                var url: URL? = null
                try {
                    url= URL(urlAsString)
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                }
                var result: JSONObject? = null
                if (url == null) {
                    return null
                }
                var connection: HttpURLConnection? = null
                try {
                    connection = url.openConnection() as HttpURLConnection
                    val inputStream = connection.inputStream
                    val inputStreamReader = InputStreamReader(inputStream)
                    val reader = BufferedReader(inputStreamReader)
                    val builder = StringBuilder()
                    var line = reader.readLine()
                    while (line != null) {
                        builder.append(line)
                        line = reader.readLine()
                    }
                    result = JSONObject(builder.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                } finally {
                    connection?.disconnect()
                }
                return result
            }

        }


        class JSONLoadTask : AsyncTask<URL, Void, JSONObject>() {
            override fun doInBackground(vararg p0: URL?): JSONObject? {
                var result: JSONObject?
                if (p0 == null) {
                    return null
                }
                var connection: HttpURLConnection? = null
                try {
                    connection = p0[0]?.openConnection() as HttpURLConnection
                    val inputStream = connection.inputStream
                    val inputStreamReader = InputStreamReader(inputStream)
                    val reader = BufferedReader(inputStreamReader)
                    val builder = StringBuilder()
                    var line = reader.readLine()
                    while (line != null) {
                        builder.append(line)
                        line = reader.readLine()
                    }
                    result = JSONObject(builder.toString())
                    return result
                } finally {
                    connection?.disconnect()
                }
            }

        }
    }




}