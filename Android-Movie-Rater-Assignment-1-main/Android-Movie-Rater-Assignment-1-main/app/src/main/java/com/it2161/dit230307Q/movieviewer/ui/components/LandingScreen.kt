package com.it2161.dit230307Q.movieviewer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.it2161.dit230307Q.movieviewer.R
import com.it2161.dit230307Q.movieviewer.data.MovieItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(navController: NavController, movieViewModel: MovieViewModel = viewModel()) {
    var selectedCategory by remember { mutableStateOf("Popular") }
    var menuExpanded by remember { mutableStateOf(false) }

    val movies by movieViewModel.movies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "PopCornMovies",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("View Profile") },
                            onClick = {
                                menuExpanded = false
                                navController.navigate("profile")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                menuExpanded = false
                                navController.navigate("login_screen") {
                                    popUpTo("landing_screen") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Category Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Popular", "Top Rated", "Now Playing", "Upcoming").forEach { category ->
                    Text(
                        text = category,
                        modifier = Modifier
                            .clickable {
                                selectedCategory = category
                                movieViewModel.fetchMovies(category)
                            }
                            .padding(8.dp),
                        color = if (selectedCategory == category) Color.Blue else Color.Black,
                        fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Movie List
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(movies) { movie ->
                    MovieItemCard(movie = movie) {
                        navController.navigate("movieDetail/${movie.title}")
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItemCard(movie: MovieItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.placeholder), // Replace with actual image loading logic
                contentDescription = "Movie Poster",
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = movie.title ?: "Unknown Title", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = movie.releaseDate ?: "Unknown Date", color = Color.Gray, fontSize = 16.sp)
                Text(text = "${movie.ratings_score}/10", color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}