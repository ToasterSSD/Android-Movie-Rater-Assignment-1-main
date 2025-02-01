package com.it2161.dit230307Q.movieviewer

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.it2161.dit230307Q.movieviewer.ui.components.*

@ExperimentalMaterial3Api
@Composable
fun MovieViewerApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val modifier = Modifier.fillMaxSize().padding(innerPadding)

        NavHost(
            navController = navController,
            startDestination = "login_screen",
            modifier = modifier
        ) {
            composable("login_screen") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("landing_screen") {
                            popUpTo("login_screen") { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate("register_screen")
                    }
                )
            }

            composable("register_screen") {
                RegisterUserScreen(
                    navController = navController,
                    onRegisterSuccess = {
                        navController.navigate("landing_screen") {
                            popUpTo("register_screen") { inclusive = true }
                        }
                    },
                    onCancel = {
                        navController.popBackStack() // Navigate back to the login screen
                    }
                )
            }

            composable("landing_screen") {
                LandingScreen(navController = navController)
            }

            composable("profile") {
                ProfileScreen(
                    navController = navController,
                    onBack = { navController.popBackStack() }
                )
            }

            composable("edit_profile_screen") {
                EditProfileScreen(
                    navController = navController,
                    onSave = { navController.popBackStack() },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("movieDetail/{movieId}", arguments = listOf(navArgument("movieId") { type = NavType.IntType })) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                MovieDetailScreen(movieId = movieId, navController = navController)
            }

            composable(
                "comment_screen/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                CommentMovieScreen(
                    movieId = movieId,
                    navController = navController
                )
            }

            composable("add_comment_screen/{movieTitle}") { backStackEntry ->
                val movieTitle = backStackEntry.arguments?.getString("movieTitle")
                if (movieTitle != null) {
                    AddCommentScreen(movieTitle = movieTitle, navController = navController)
                } else {
                    // Handle the error case where movieTitle is null or invalid
                }
            }
        }
    }
}