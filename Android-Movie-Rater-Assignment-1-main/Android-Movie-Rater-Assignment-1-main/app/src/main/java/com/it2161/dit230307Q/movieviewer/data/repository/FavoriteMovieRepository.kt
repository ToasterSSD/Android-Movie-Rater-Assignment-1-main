package com.it2161.dit230307Q.movieviewer.data.repository

import com.it2161.dit230307Q.movieviewer.data.FavoriteMovie
import com.it2161.dit230307Q.movieviewer.data.FavoriteMovieDao

class FavoriteMovieRepository(private val favoriteMovieDao: FavoriteMovieDao) {
    suspend fun insertFavoriteMovie(favoriteMovie: FavoriteMovie) {
        favoriteMovieDao.insertFavoriteMovie(favoriteMovie)
    }

    suspend fun getFavoriteMovies(userId: String): List<FavoriteMovie> {
        return favoriteMovieDao.getFavoriteMovies(userId.toString())
    }

    suspend fun deleteFavoriteMovie(movieId: Int, userId: String) {
        favoriteMovieDao.deleteFavoriteMovie(movieId, userId.toString())
    }

    suspend fun isFavoriteMovie(movieId: Int, userName: String): Boolean {
        return favoriteMovieDao.getFavoriteMovies(userName).any { it.movieId == movieId }
    }
}