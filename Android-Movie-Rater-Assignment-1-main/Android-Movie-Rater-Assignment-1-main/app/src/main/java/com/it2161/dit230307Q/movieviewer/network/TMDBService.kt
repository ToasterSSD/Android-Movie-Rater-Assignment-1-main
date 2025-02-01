package com.it2161.dit230307Q.movieviewer.network

import com.it2161.dit230307Q.movieviewer.model.MovieDetailResponse
import com.it2161.dit230307Q.movieviewer.model.ConfigurationResponse
import com.it2161.dit230307Q.movieviewer.model.MovieResponse
import com.it2161.dit230307Q.movieviewer.model.MovieImagesResponse
import com.it2161.dit230307Q.movieviewer.model.MovieReviewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBService {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") apiKey: String): MovieResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("api_key") apiKey: String): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("api_key") apiKey: String): MovieResponse

    @GET("configuration")
    suspend fun getConfiguration(@Query("api_key") apiKey: String): ConfigurationResponse

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): MovieImagesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): MovieDetailResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): MovieReviewsResponse
}