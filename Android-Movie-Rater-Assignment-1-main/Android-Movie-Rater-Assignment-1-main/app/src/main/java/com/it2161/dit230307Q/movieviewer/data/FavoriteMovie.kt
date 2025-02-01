package com.it2161.dit230307Q.movieviewer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey val movieId: Int,
    val userName: String,
    val title: String,
    val overview: String,
    val posterPath: String,
    val voteAverage: Float,
    val file_path: String
)