package com.it2161.dit230307Q.movieviewer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.it2161.dit230307Q.movieviewer.data.MovieItem
import com.it2161.dit230307Q.movieviewer.model.ConfigurationResponse
import com.it2161.dit230307Q.movieviewer.model.MovieImagesResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(navController: NavController, movieViewModel: MovieViewModel = viewModel()) {
    val movies by movieViewModel.movies.collectAsState()
    val configuration by remember { mutableStateOf(movieViewModel.configuration) }
    val movieImages by movieViewModel.movieImages.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Popular") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PopCornMovie") },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                menuExpanded = false
                                navController.navigate("profile")
                            },
                            text = { Text("View Profile") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                menuExpanded = false
                                navController.navigate("login_screen")
                            },
                            text = { Text("Logout") }
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { selectedCategory = "Popular"; movieViewModel.fetchMovies("Popular") }) {
                    Text("Popular")
                }
                Button(onClick = { selectedCategory = "Top Rated"; movieViewModel.fetchMovies("Top Rated") }) {
                    Text("Top Rated")
                }
                Button(onClick = { selectedCategory = "Now Playing"; movieViewModel.fetchMovies("Now Playing") }) {
                    Text("Now Playing")
                }
                Button(onClick = { selectedCategory = "Upcoming"; movieViewModel.fetchMovies("Upcoming") }) {
                    Text("Upcoming")
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(movies) { movie ->
                    val images = movieImages[movie.id]
                    MovieItemCard(movie, configuration, images) {
                        navController.navigate("movieDetail/${movie.id}")
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun MovieItemCard(movie: MovieItem, configuration: ConfigurationResponse?, movieImages: MovieImagesResponse?, onClick: () -> Unit) {
    val backdropUrl = movieImages?.backdrops?.firstOrNull()?.file_path?.let { configuration?.images?.secure_base_url + configuration?.images?.backdrop_sizes?.get(1) + it }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(backdropUrl),
                contentDescription = "Backdrop for ${movie.title}",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rating: %.1f".format(movie.vote_average),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star Icon",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}