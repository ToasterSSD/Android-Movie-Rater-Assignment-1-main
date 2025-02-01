package com.it2161.dit230307Q.movieviewer.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.it2161.dit230307Q.movieviewer.R

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val userName: String = "",
    val password: String = "",
    val email: String = "",
    val gender: String = "",
    val mobile: String = "",
    val updates: Boolean = false,
    val yob: String = "",
    val avatar: Int = R.drawable.avatar_1
)