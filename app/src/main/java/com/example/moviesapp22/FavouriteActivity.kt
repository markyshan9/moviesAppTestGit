package com.example.moviesapp22

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp22.adapters.MovieAdapter
import com.example.moviesapp22.data.FavouriteMovie
import com.example.moviesapp22.data.MainViewModel
import com.example.moviesapp22.data.Movie

class FavouriteActivity : AppCompatActivity() {

    private lateinit var recyclerViewFavouriteMovies: RecyclerView
    private lateinit var adapter: MovieAdapter
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        recyclerViewFavouriteMovies = findViewById(R.id.recyclerViewFavouriteMovies)
        adapter = MovieAdapter()
        recyclerViewFavouriteMovies.adapter = adapter
        recyclerViewFavouriteMovies.layoutManager = GridLayoutManager(this, 2)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val favouriteMovies : LiveData<MutableList<FavouriteMovie>> = viewModel.getFavouriteMovies()
        favouriteMovies.observe(this, object : Observer<MutableList<FavouriteMovie>> {
            override fun onChanged(t: MutableList<FavouriteMovie>?) {
                if(t != null) {
                    val movies = mutableListOf<Movie>()
                    movies.addAll(t)
                        adapter.setMovies(movies = movies )
                }
            }

        })

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
}