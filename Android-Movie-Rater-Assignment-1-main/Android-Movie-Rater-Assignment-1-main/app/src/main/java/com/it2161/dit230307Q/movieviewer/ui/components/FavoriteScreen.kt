@file:OptIn(ExperimentalMaterial3Api::class)

package com.it2161.dit230307Q.movieviewer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.it2161.dit230307Q.movieviewer.model.MovieResponse

@Composable
fun FavoriteScreen(navController: NavController, movieViewModel: MovieViewModel = viewModel()) {
    val favoriteMovies by movieViewModel.favoriteMovies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite Movies") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            items(favoriteMovies) { favoriteMovie ->
                val movieResponse = MovieResponse(
                    id = favoriteMovie.movieId,
                    title = favoriteMovie.title,
                    overview = favoriteMovie.overview,
                    poster_path = favoriteMovie.posterPath,
                    vote_average = favoriteMovie.voteAverage
                )
                MovieItemCard(
                    movie = movieResponse,
                    configuration = null,
                    movieImages = null,
                    userName = favoriteMovie.userName
                ) {
                    navController.navigate("movieDetail/${favoriteMovie.movieId}")
                }
            }
        }
    }
}