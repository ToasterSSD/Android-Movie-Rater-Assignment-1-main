package com.it2161.dit230307Q.movieviewer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.it2161.dit230307Q.movieviewer.model.MovieEntity

@Database(entities = [UserProfile::class, FavoriteMovie::class, MovieEntity::class], version = 6, exportSchema = false)
abstract class UserProfileDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: UserProfileDatabase? = null

        fun getDatabase(context: Context): UserProfileDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserProfileDatabase::class.java,
                    "user_profile_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instance.also { INSTANCE = it }
            }
        }
    }
}