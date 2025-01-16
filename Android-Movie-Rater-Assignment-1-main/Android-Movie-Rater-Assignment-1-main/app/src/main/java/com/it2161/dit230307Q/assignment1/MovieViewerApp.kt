package com.it2161.dit230307Q.assignment1

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
import com.it2161.dit230307Q.assignment1.ui.components.AddCommentScreen
import com.it2161.dit230307Q.assignment1.ui.components.CommentMovieScreen
import com.it2161.dit230307Q.assignment1.ui.components.EditProfileScreen
import com.it2161.dit230307Q.assignment1.ui.components.LandingScreen
import com.it2161.dit230307Q.assignment1.ui.components.LoginScreen
import com.it2161.dit230307Q.assignment1.ui.components.MovieDetailScreen
import com.it2161.dit230307Q.assignment1.ui.components.ProfileScreen
import com.it2161.dit230307Q.assignment1.ui.components.RegisterUserScreen

@ExperimentalMaterial3Api
@Composable
fun MovieViewerApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val modifier = Modifier.fillMaxSize().padding(innerPadding)
        Log.d("App data: ", "${MovieRaterApplication.instance.data.size}")
        if (MovieRaterApplication.instance.userProfile != null) {
            Log.d("User profile: ", "${MovieRaterApplication.instance.userProfile!!.userName}")
        } else {
            Log.d("User profile: ", "No user profile saved")
        }

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

            composable("movieDetail/{movieTitle}") { backStackEntry ->
                val movieTitle = backStackEntry.arguments?.getString("movieTitle")
                MovieDetailScreen(movieTitle = movieTitle, navController = navController)
            }

            composable(
                "comment_screen/{movieTitle}/{commentUser}/{commentText}/{commentDate}/{commentTime}",
                arguments = listOf(
                    navArgument("movieTitle") { type = NavType.StringType },
                    navArgument("commentUser") { type = NavType.StringType },
                    navArgument("commentText") { type = NavType.StringType },
                    navArgument("commentDate") { type = NavType.StringType },
                    navArgument("commentTime") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                CommentMovieScreen(
                    navController = navController,
                    movieTitle = backStackEntry.arguments?.getString("movieTitle"),
                    commentUser = backStackEntry.arguments?.getString("commentUser"),
                    commentText = backStackEntry.arguments?.getString("commentText"),
                    commentDate = backStackEntry.arguments?.getString("commentDate"),
                    commentTime = backStackEntry.arguments?.getString("commentTime")
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