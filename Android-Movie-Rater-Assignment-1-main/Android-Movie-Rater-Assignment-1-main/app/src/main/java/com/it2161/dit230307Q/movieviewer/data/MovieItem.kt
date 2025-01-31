package com.it2161.dit230307Q.movieviewer.data


data class MovieItem(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val vote_average: Float,
    val director: String,
    val releaseDate: String,
    val ratings_score: Float,
    val actors: List<String>,
    val image: String,
    val genre: String,
    val length: Int,
    val synopsis: String,
    var comment: List<Comments>
)

data class Comments(
    val user: String,
    val comment: String,
    val date: String,
    val time: String,
)


