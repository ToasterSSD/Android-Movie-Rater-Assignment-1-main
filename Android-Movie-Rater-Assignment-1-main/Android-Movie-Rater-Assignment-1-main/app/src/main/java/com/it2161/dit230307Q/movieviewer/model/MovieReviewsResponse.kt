package com.it2161.dit230307Q.movieviewer.model

data class MovieReviewsResponse(
    val id: Int,
    val page: Int,
    val results: List<Review>,
    val total_pages: Int,
    val total_results: Int
)

data class Review(
    val id: String,
    val author: String,
    val content: String,
    val created_at: String
)