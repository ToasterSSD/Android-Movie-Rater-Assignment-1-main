package com.it2161.dit230307Q.movieviewer.model

data class MovieDetailResponse(
    val id: Int,
    val backdrop_path: String?,
    val adult: Boolean,
    val genres: List<Genre>,
    val original_language: String,
    val release_date: String,
    val runtime: Int,
    val vote_count: Int,
    val title: String,
    val vote_average: Float,
    val overview: String,
    val revenue: Long,
    val problematic_field: String
)

data class Genre(
    val id: Int,
    val name: String
)