package com.it2161.dit230307Q.movieviewer.model

fun MovieEntity.toResponse(): MovieResponse {
    return MovieResponse(
        id = id,
        title = title,
        overview = overview,
        poster_path = poster_path,
        vote_average = vote_average
    )
}