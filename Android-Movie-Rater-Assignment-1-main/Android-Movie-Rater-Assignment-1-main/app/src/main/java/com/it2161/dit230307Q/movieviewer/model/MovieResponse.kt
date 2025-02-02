package com.it2161.dit230307Q.movieviewer.model

data class MovieResponse(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val vote_average: Float
) {
    fun toEntity(category: String): MovieEntity {
        return MovieEntity(
            id = id,
            title = title,
            overview = overview,
            poster_path = poster_path,
            vote_average = vote_average,
            category = category
        )
    }
}