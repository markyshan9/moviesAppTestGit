package com.example.moviesapp22.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.semantics.SemanticsActions.OnClick
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp22.R
import com.example.moviesapp22.data.Trailer

class TrailerAdapter : RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {

    private var onTrailerClickListener: OnTrailerClickListener? = null
    private var trailers: MutableList<Trailer> = mutableListOf()

    interface OnTrailerClickListener {
        fun onTrailerClick(url: String)
    }

    fun setOnTrailerClickListener(onTrailerClickListener: OnTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener
    }

    fun setTrailers(trailers: MutableList<Trailer>) {
        this.trailers = trailers
        notifyDataSetChanged()
    }

    inner class TrailerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val textViewNameOfVideo: TextView = itemView.findViewById(
            R.id.textViewNameOfVideo
        )

        init {
            itemView.setOnClickListener(View.OnClickListener() {
                onClick(itemView)
            })
        }

        override fun onClick(p0: View?) {
            if (onTrailerClickListener != null) {
                onTrailerClickListener?.onTrailerClick(trailers.get(adapterPosition).key)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trailer_item, parent, false)
        return TrailerViewHolder(itemView = view)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val trailer = trailers.get(position)
        holder.textViewNameOfVideo.text = trailer.name

    }

    override fun getItemCount(): Int {
        return trailers.size
    }


}