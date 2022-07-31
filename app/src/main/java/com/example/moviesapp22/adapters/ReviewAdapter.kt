package com.example.moviesapp22.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp22.R
import com.example.moviesapp22.data.Review

class ReviewAdapter() : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    private var reviews: MutableList<Review> = mutableListOf()

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewAuthor : TextView = itemView.findViewById(R.id.textViewAuthor)
        val textViewContent : TextView = itemView.findViewById(R.id.textViewContent)
    }

    fun setReviews(reviews: MutableList<Review>) {
        this.reviews = reviews
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(itemView = view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.textViewAuthor.text = review.author
        holder.textViewContent.text = review.content
    }

    override fun getItemCount(): Int {
        return reviews.size
    }
}