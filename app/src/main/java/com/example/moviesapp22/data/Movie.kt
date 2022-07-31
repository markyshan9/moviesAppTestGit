package com.example.moviesapp22.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
open class Movie(
    @PrimaryKey(autoGenerate = true)
    val uniqueId: Int,
    val id: Int,
    val voteCount: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String,
    val bigPosterPath: String,
    val backdropPath: String,
    val voteAverage: Double,
    val releaseDate: String
) {
    @Ignore
    constructor(
        id: Int,
        voteCount: Int,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        bigPosterPath: String,
        backdropPath: String,
        voteAverage: Double,
        releaseDate: String) : this(
        uniqueId = 0,
        id = id,
        voteCount = voteCount,
        title = title,
        originalTitle = originalTitle,
        overview = overview,
        posterPath = posterPath,
        bigPosterPath = bigPosterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        releaseDate = releaseDate
    )

}