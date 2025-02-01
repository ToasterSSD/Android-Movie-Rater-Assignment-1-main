package com.it2161.dit230307Q.movieviewer

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.it2161.dit230307Q.movieviewer.ui.components.*
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun MovieViewerApp() {
    val navController = rememberNavController()
    val viewModel: UserProfileViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.loadUserProfile("defaultUserName")
        }
    }

    val userProfile by viewModel.userProfile.collectAsState()

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
                    },
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable("register_screen") {
                RegisterUserScreen(
                    navController = navController,
                    onRegisterSuccess = {
                        navController.navigate("login_screen") {
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
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }

            composable("edit_profile_screen") {
                EditProfileScreen(
                    navController = navController,
                    onSave = { navController.popBackStack() },
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }

            composable("movieDetail/{movieId}", arguments = listOf(navArgument("movieId") { type = NavType.IntType })) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                MovieDetailScreen(movieId = movieId, navController = navController)
            }

            composable(
                "comment_screen/{movieId}/{reviewId}",
                arguments = listOf(
                    navArgument("movieId") { type = NavType.IntType },
                    navArgument("reviewId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                val reviewId = backStackEntry.arguments?.getString("reviewId") ?: ""
                CommentMovieScreen(
                    movieId = movieId,
                    reviewId = reviewId,
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