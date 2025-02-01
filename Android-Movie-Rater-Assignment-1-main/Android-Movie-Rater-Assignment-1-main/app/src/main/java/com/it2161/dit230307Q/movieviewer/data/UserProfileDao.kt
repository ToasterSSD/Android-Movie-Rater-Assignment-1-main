package com.it2161.dit230307Q.movieviewer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)

    @Query("SELECT * FROM user_profiles WHERE userName = :userName")
    suspend fun getUserProfile(userName: String): UserProfile?

    @Update
    suspend fun updateUserProfile(userProfile: UserProfile)


}