package com.it2161.dit230307Q.movieviewer.data.repository

import android.content.Context
import com.it2161.dit230307Q.movieviewer.R
import com.it2161.dit230307Q.movieviewer.data.UserProfileDatabase
import com.it2161.dit230307Q.movieviewer.model.ConfigurationResponse
import com.it2161.dit230307Q.movieviewer.model.MovieDetailResponse
import com.it2161.dit230307Q.movieviewer.model.MovieEntity
import com.it2161.dit230307Q.movieviewer.model.MovieImagesResponse
import com.it2161.dit230307Q.movieviewer.model.MovieResponse
import com.it2161.dit230307Q.movieviewer.model.MovieReviewsResponse
import com.it2161.dit230307Q.movieviewer.model.toResponse
import com.it2161.dit230307Q.movieviewer.network.TMDBService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRepository(context: Context) {
    private val apiKey: String = context.getString(R.string.tmdb_api_key)
    private val service: TMDBService
    private val movieDao = UserProfileDatabase.getDatabase(context).movieDao()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(TMDBService::class.java)
    }

    suspend fun getPopularMovies(): List<MovieResponse> {
        return try {
            val movies = service.getPopularMovies(apiKey).results
            movieDao.insertMovies(movies.map { it.toEntity("Popular") })
            movies
        } catch (e: Exception) {
            movieDao.getMoviesByCategory("Popular").map { it.toResponse() }
        }
    }

    suspend fun getTopRatedMovies(): List<MovieResponse> {
        return try {
            val movies = service.getTopRatedMovies(apiKey).results
            movieDao.insertMovies(movies.map { it.toEntity("Top Rated") })
            movies
        } catch (e: Exception) {
            movieDao.getMoviesByCategory("Top Rated").map { it.toResponse() }
        }
    }

    suspend fun getNowPlayingMovies(): List<MovieResponse> {
        return try {
            val movies = service.getNowPlayingMovies(apiKey).results
            movieDao.insertMovies(movies.map { it.toEntity("Now Playing") })
            movies
        } catch (e: Exception) {
            movieDao.getMoviesByCategory("Now Playing").map { it.toResponse() }
        }
    }

    suspend fun getUpcomingMovies(): List<MovieResponse> {
        return try {
            val movies = service.getUpcomingMovies(apiKey).results
            movieDao.insertMovies(movies.map { it.toEntity("Upcoming") })
            movies
        } catch (e: Exception) {
            movieDao.getMoviesByCategory("Upcoming").map { it.toResponse() }
        }
    }

    suspend fun insertMovies(movies: List<MovieEntity>) {
        movieDao.insertMovies(movies)
    }

    suspend fun getMoviesByCategory(category: String): List<MovieEntity> {
        return movieDao.getMoviesByCategory(category)
    }

    suspend fun getConfiguration(): ConfigurationResponse = service.getConfiguration(apiKey)

    suspend fun getMovieImages(movieId: Int): MovieImagesResponse = service.getMovieImages(movieId, apiKey)

    suspend fun getMovieDetails(movieId: Int): MovieDetailResponse = service.getMovieDetails(movieId, apiKey)

    suspend fun getMovieReviews(movieId: Int): MovieReviewsResponse = service.getMovieReviews(movieId, apiKey)

    suspend fun searchMovies(query: String): List<MovieResponse> {
        return try {
            val movies = service.searchMovies(apiKey, query).results
            movieDao.insertMovies(movies.map { it.toEntity("Search") })
            movies
        } catch (e: Exception) {
            movieDao.getMoviesByCategory("Search").map { it.toResponse() }
        }
    }

    suspend fun getSimilarMovies(movieId: Int): List<MovieResponse> {
        return try {
            val movies = service.getSimilarMovies(movieId, apiKey).results
            movieDao.insertMovies(movies.map { it.toEntity("Similar") })
            movies
        } catch (e: Exception) {
            movieDao.getMoviesByCategory("Similar").map { it.toResponse() }
        }
    }
}