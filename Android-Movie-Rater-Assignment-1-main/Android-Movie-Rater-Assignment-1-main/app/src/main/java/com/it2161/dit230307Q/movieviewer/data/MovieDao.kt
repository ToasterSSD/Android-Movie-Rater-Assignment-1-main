package com.it2161.dit230307Q.movieviewer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.it2161.dit230307Q.movieviewer.model.MovieEntity

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE category = :category")
    suspend fun getMoviesByCategory(category: String): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
}