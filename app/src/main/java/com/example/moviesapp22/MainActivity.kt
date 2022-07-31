package com.example.moviesapp22

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.utils.JSONUtils
import com.example.moviesapp22.utils.NetworkUtils
import com.example.moviesapp22.adapters.MovieAdapter
import com.example.moviesapp22.data.MainViewModel
import com.example.moviesapp22.data.Movie
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<JSONObject> {


    private lateinit var recyclerViewPosters: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var switchSort: Switch
    private lateinit var textViewPopularity: TextView
    private lateinit var textViewTopRated: TextView
    private lateinit var viewModel: MainViewModel
    private lateinit var progressBar: ProgressBar


    private val LOADER_ID : Int = 1795;
    private lateinit var loaderManager: LoaderManager
    private var methodOfSort: Int = 0

    private var page: Int = 1
    private var isLoading : Boolean = false
    private lateinit var lang: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lang = Locale.getDefault().language
        loaderManager = LoaderManager.getInstance(this)
        textViewPopularity = findViewById(R.id.textViewPopularity)
        textViewTopRated = findViewById(R.id.textViewTopRated)
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters)
        progressBar = findViewById(R.id.progressBarLoading)
        switchSort = findViewById(R.id.switchSort)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        movieAdapter = MovieAdapter()
        recyclerViewPosters.adapter = movieAdapter
        recyclerViewPosters.layoutManager = GridLayoutManager(this, getColumnCount())
        switchSort.isChecked = true
        switchSort.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { button, isChecked ->
            page = 1
            setMethodOfSort(isChecked)
        })
        switchSort.isChecked = false
        movieAdapter.setOnPosterClickListener(object : MovieAdapter.OnPosterClickListener {
            override fun onPosterClick(position: Int) {
                val movie = movieAdapter.getMovies().get(position)
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("id", movie.id)
                startActivity(intent)

            }
        })
        movieAdapter.setOnReachEndListener(object : MovieAdapter.OnReachEndListener {
            override fun onReachEnd() {
                if(!isLoading) {
                    downloadData(methodOfSort = methodOfSort, page = page)
                }
            }
        })
        val moviesFromLiveData: LiveData<MutableList<Movie>> = viewModel.getMovies()
        moviesFromLiveData.observe(this, object : Observer<MutableList<Movie>> {
            override fun onChanged(t: MutableList<Movie>?) {
                if (page == 1 && t != null) {
                    movieAdapter.setMovies(movies = t)
                }

            }
        })
    }

    fun getColumnCount() : Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width : Int = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        return if(width / 185 > 2) {
            width / 185
        } else 2
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.itemMain -> startActivity(Intent(this, MainActivity::class.java))
            R.id.itemFavourite -> startActivity(Intent(this, FavouriteActivity::class.java))

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setMethodOfSort(isTopRated: Boolean) {
        if (isTopRated) {
            methodOfSort = NetworkUtils.TOP_RATED
            textViewPopularity.setTextColor(resources.getColor(R.color.white))
            textViewTopRated.setTextColor(resources.getColor(R.color.purple_200))
        } else {
            methodOfSort = NetworkUtils.POPULARITY
            textViewTopRated.setTextColor(resources.getColor(R.color.white))
            textViewPopularity.setTextColor(resources.getColor(R.color.purple_200))
        }
        downloadData(methodOfSort = methodOfSort, page = page)
    }

    private fun downloadData(methodOfSort: Int, page: Int) {
        val url = NetworkUtils.buildURL(sortBy = methodOfSort, page = page, lang = lang)
        val bundle = Bundle()
        bundle.putString("url", url.toString())
        loaderManager.restartLoader(LOADER_ID, bundle, this)
    }

    fun onClickSetPopularity(view: View) {
        setMethodOfSort(isTopRated = false)
        switchSort.isChecked = false

    }

    fun onClickSetTopRated(view: View) {
        setMethodOfSort(isTopRated = true)
        switchSort.isChecked = true
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<JSONObject> {
        val jsonLoader = NetworkUtils.Companion.JSONLoader(this, args)
        jsonLoader.setOnStartLoadingListener(object : NetworkUtils.Companion.JSONLoader.OnStartLoadingListener {
            override fun onStartLoading() {
                progressBar.visibility = View.VISIBLE
                isLoading = true
            }

        })
        return jsonLoader
    }

    override fun onLoadFinished(loader: Loader<JSONObject>, data: JSONObject?) {
        val movies: MutableList<Movie> = JSONUtils.getMovieFromJSON(jsonObject = data)
        if (movies != null && movies.isNotEmpty()) {
            if(page == 1) {
                viewModel.deleteAllMovies()
                movieAdapter.clear()
            }
            for (movie in movies) {
                viewModel.insertMovie(movie)
            }
            movieAdapter.addMovies(movies = movies)
            page++
        }
        isLoading = false
        progressBar.visibility = View.INVISIBLE
        loaderManager.destroyLoader(LOADER_ID)
    }

    override fun onLoaderReset(loader: Loader<JSONObject>) {
        TODO("Not yet implemented")
    }
}