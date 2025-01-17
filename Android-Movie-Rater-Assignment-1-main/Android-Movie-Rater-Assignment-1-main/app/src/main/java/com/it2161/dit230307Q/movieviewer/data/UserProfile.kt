package com.it2161.dit230307Q.movieviewer.data

import com.it2161.dit230307Q.movieviewer.R

data class UserProfile(
    val userName: String = "",
    val password: String = "",
    val email: String = "",
    val gender: String = "",
    val mobile: String = "",
    val updates: Boolean = false,
    val yob: String = "",
    val avatar: Int = R.drawable.avatar_1
)