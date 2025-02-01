package com.it2161.dit230307Q.movieviewer.data.repository

import com.it2161.dit230307Q.movieviewer.data.UserProfile
import com.it2161.dit230307Q.movieviewer.data.UserProfileDao

class UserProfileRepository(private val userProfileDao: UserProfileDao) {
    suspend fun insertUserProfile(userProfile: UserProfile) {
        userProfileDao.insertUserProfile(userProfile)
    }

    suspend fun getUserProfile(userName: String): UserProfile? {
        return userProfileDao.getUserProfile(userName)
    }

    suspend fun updateUserProfile(userProfile: UserProfile) {
        userProfileDao.updateUserProfile(userProfile)
    }


}