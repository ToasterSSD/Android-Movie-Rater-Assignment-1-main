package com.it2161.dit230307Q.movieviewer.data.repository

import android.content.Context
import com.it2161.dit230307Q.movieviewer.R
import com.it2161.dit230307Q.movieviewer.model.MovieResponse
import com.it2161.dit230307Q.movieviewer.network.TMDBService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    suspend fun getPopularMovies(): MovieResponse = service.getPopularMovies(apiKey)
    suspend fun getTopRatedMovies(): MovieResponse = service.getTopRatedMovies(apiKey)
    suspend fun getNowPlayingMovies(): MovieResponse = service.getNowPlayingMovies(apiKey)
    suspend fun getUpcomingMovies(): MovieResponse = service.getUpcomingMovies(apiKey)
}