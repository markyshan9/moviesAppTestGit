package com.example.moviesapp22

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.utils.JSONUtils
import com.example.moviesapp22.utils.NetworkUtils
import com.example.moviesapp22.adapters.ReviewAdapter
import com.example.moviesapp22.adapters.TrailerAdapter
import com.example.moviesapp22.data.*
import com.squareup.picasso.Picasso
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var imageViewAddToFavourite : ImageView
    private lateinit var imageViewBigPoster: ImageView
    private lateinit var textViewTitle : TextView
    private lateinit var textViewOriginalTitle : TextView
    private lateinit var textViewRating : TextView
    private lateinit var textViewReleaseDate : TextView
    private lateinit var textViewOverview : TextView
    private lateinit var viewModel: MainViewModel
    private lateinit var movie : Movie
    private lateinit var scrollViewInfo: ScrollView

    private lateinit var recyclerViewTrailer: RecyclerView
    private lateinit var recyclerViewReview: RecyclerView
    private lateinit var trailerAdapter: TrailerAdapter
    private lateinit var reviewAdapter: ReviewAdapter

    private var id : Number = -1
    private var favouriteMovie: FavouriteMovie? = null
    private lateinit var lang: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        lang = Locale.getDefault().language
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster)
        textViewTitle = findViewById(R.id.textViewTitle)
        scrollViewInfo = findViewById(R.id.scrollViewInfo)
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle)
        textViewRating = findViewById(R.id.textViewRating)
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate)
        textViewOverview = findViewById(R.id.textViewOverview)
        imageViewAddToFavourite = findViewById(R.id.imageViewAddToFavorite)

        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1)
        } else {
            finish()
        }

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        movie = viewModel.getMovieById(id = id.toInt())
        Picasso.get().load(movie.bigPosterPath).into(imageViewBigPoster)
        textViewTitle.text = movie.title
        textViewOriginalTitle.text = movie.originalTitle
        textViewOverview.text = movie.overview
        textViewReleaseDate.text = movie.releaseDate
        textViewRating.text = movie.voteAverage.toString()
        setFavourite()
        recyclerViewReview = findViewById(R.id.recyclerViewReviews)
        recyclerViewTrailer = findViewById(R.id.recyclerViewTrailer)
        trailerAdapter = TrailerAdapter()
        trailerAdapter.setOnTrailerClickListener(object : TrailerAdapter.OnTrailerClickListener {
            override fun onTrailerClick(url: String) {
                val intentToTrailer = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intentToTrailer)
            }

        })
        reviewAdapter = ReviewAdapter()
        recyclerViewTrailer.layoutManager = LinearLayoutManager(this)
        recyclerViewReview.layoutManager = LinearLayoutManager(this)
        recyclerViewReview.adapter = reviewAdapter
        recyclerViewTrailer.adapter = trailerAdapter
        val jsonObjectTrailers = NetworkUtils.getJSONForVideos(id = movie.id, lang = lang)
        val jsonObjectReviews = NetworkUtils.getJSONForReviews(id = movie.id, lang = lang)
        val trailers: MutableList<Trailer> = JSONUtils.getTrailerFromJSON(jsonObject = jsonObjectTrailers)
        val reviews : MutableList<Review> = JSONUtils.getReviewsFromJSON(jsonObject = jsonObjectReviews)
        reviewAdapter.setReviews(reviews = reviews)
        trailerAdapter.setTrailers(trailers = trailers)
        scrollViewInfo.smoothScrollTo(0,0)
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

    fun onClickChangeFavourite(view: View) {

        if(favouriteMovie == null) {
            viewModel.insertFavouriteMovie(movie = FavouriteMovie(movie = movie))
            Toast.makeText(this, getString(R.string.add_to_favourite), Toast.LENGTH_SHORT).show()
        } else {
            viewModel.deleteFavouriteMovie(favouriteMovie!!)
            Toast.makeText(this, getString(R.string.remove_from_favourite), Toast.LENGTH_SHORT).show()
        }
        setFavourite()
    }

    private fun setFavourite() {
        favouriteMovie = viewModel.getFavouriteMovieById(id = id.toInt())
        if (favouriteMovie == null) {
            imageViewAddToFavourite.setImageResource(R.drawable.favourite_add_to)
        } else {
            imageViewAddToFavourite.setImageResource(R.drawable.favourite_remove)
        }
    }
}