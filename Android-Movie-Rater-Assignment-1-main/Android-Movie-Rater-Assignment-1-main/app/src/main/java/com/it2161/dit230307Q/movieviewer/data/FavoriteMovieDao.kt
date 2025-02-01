package com.it2161.dit230307Q.movieviewer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(favoriteMovie: FavoriteMovie)

    @Query("SELECT * FROM favorite_movies WHERE userName = :userName")
    suspend fun getFavoriteMovies(userName: String): List<FavoriteMovie>

    @Query("DELETE FROM favorite_movies WHERE movieId = :movieId AND userName = :userName")
    suspend fun deleteFavoriteMovie(movieId: Int, userName: String)
}