package com.it2161.dit230307Q.movieviewer.data.repository

import android.content.Context
import com.it2161.dit230307Q.movieviewer.R
import com.it2161.dit230307Q.movieviewer.model.ConfigurationResponse
import com.it2161.dit230307Q.movieviewer.model.MovieDetailResponse
import com.it2161.dit230307Q.movieviewer.model.MovieResponse
import com.it2161.dit230307Q.movieviewer.model.MovieImagesResponse
import com.it2161.dit230307Q.movieviewer.network.TMDBService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.it2161.dit230307Q.movieviewer.model.MovieReviewsResponse

class MovieRepository(context: Context) {
    private val apiKey: String = context.getString(R.string.tmdb_api_key)
    private val service: TMDBService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(TMDBService::class.java)
    }

    suspend fun getPopularMovies(): List<MovieResponse> = service.getPopularMovies(apiKey).results
    suspend fun getTopRatedMovies(): List<MovieResponse> = service.getTopRatedMovies(apiKey).results
    suspend fun getNowPlayingMovies(): List<MovieResponse> = service.getNowPlayingMovies(apiKey).results
    suspend fun getUpcomingMovies(): List<MovieResponse> = service.getUpcomingMovies(apiKey).results
    suspend fun getConfiguration(): ConfigurationResponse = service.getConfiguration(apiKey)
    suspend fun getMovieImages(movieId: Int): MovieImagesResponse = service.getMovieImages(movieId, apiKey)
    suspend fun getMovieDetails(movieId: Int): MovieDetailResponse = service.getMovieDetails(movieId, apiKey)
    suspend fun getMovieReviews(movieId: Int): MovieReviewsResponse = service.getMovieReviews(movieId, apiKey)
    suspend fun searchMovies(query: String): List<MovieResponse> = service.searchMovies(apiKey, query).results
    suspend fun getSimilarMovies(movieId: Int): List<MovieResponse> = service.getSimilarMovies(movieId, apiKey).results

}