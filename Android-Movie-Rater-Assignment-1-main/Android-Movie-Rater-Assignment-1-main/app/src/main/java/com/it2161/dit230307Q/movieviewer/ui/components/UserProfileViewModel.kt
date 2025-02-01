package com.it2161.dit230307Q.movieviewer.ui.components

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.it2161.dit230307Q.movieviewer.MovieRaterApplication
import com.it2161.dit230307Q.movieviewer.data.UserProfile
import com.it2161.dit230307Q.movieviewer.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserProfileRepository

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    init {
        val userProfileDao = (application as MovieRaterApplication).database.userProfileDao()
        repository = UserProfileRepository(userProfileDao)
    }

    fun insertUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            repository.insertUserProfile(userProfile)
        }
    }

    suspend fun getUserProfile(userName: String): UserProfile? {
        return repository.getUserProfile(userName)
    }

    fun loadUserProfile(userName: String) {
        viewModelScope.launch {
            _userProfile.value = repository.getUserProfile(userName)
        }
    }

    fun updateUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            repository.updateUserProfile(userProfile)
            _userProfile.value = userProfile
            MovieRaterApplication.instance.userProfile = userProfile
        }
    }
}