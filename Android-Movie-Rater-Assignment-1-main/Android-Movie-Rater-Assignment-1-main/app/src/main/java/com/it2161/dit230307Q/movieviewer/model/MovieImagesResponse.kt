package com.it2161.dit230307Q.movieviewer.model

data class MovieImagesResponse(
    val id: Int,
    val backdrops: List<Image>,
    val posters: List<Image>
)

data class Image(
    val file_path: String,
    val width: Int,
    val height: Int,
    val aspect_ratio: Float,
    val vote_average: Float,
    val vote_count: Int
)