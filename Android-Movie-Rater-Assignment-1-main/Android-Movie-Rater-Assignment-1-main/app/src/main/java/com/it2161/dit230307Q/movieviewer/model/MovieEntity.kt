package com.it2161.dit230307Q.movieviewer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val vote_average: Float,
    val category: String
)