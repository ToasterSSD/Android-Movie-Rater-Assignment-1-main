package com.it2161.dit230307Q.movieviewer.ui.components

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.it2161.dit230307Q.movieviewer.data.UserProfile
import com.it2161.dit230307Q.movieviewer.model.ConfigurationResponse
import com.it2161.dit230307Q.movieviewer.model.MovieImagesResponse
import com.it2161.dit230307Q.movieviewer.model.MovieResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(navController: NavController, userProfile: UserProfile?, application: Application) {
    val movieViewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(application, SavedStateHandle()))
    val movies by movieViewModel.movies.collectAsState()
    var configuration by remember { mutableStateOf<ConfigurationResponse?>(null) }
    val movieImages by movieViewModel.movieImages.collectAsState()
    val errorMessage by movieViewModel.errorMessage.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Popular") }
    var searchQuery by remember { mutableStateOf(movieViewModel.searchQuery) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        movieViewModel.fetchMovies("Popular")
        movieViewModel.fetchConfiguration()
        isLoading = false
    }

    LaunchedEffect(movieViewModel.configuration) {
        configuration = movieViewModel.configuration
    }

    LaunchedEffect(movies) {
        movies.forEach { movie ->
            val imageUrl = movieImages[movie.id]?.backdrops?.firstOrNull()?.file_path?.let { filePath ->
                configuration?.images?.secure_base_url?.let { baseUrl ->
                    baseUrl + configuration!!.images.backdrop_sizes[1] + filePath
                }
            }
            imageUrl?.let {
                preloadImage(context, it)
            }
        }
    }

    if (isLoading) {
        LoadingScreen()
    } else {
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
                                    navController.navigate("favorite_screen")
                                },
                                text = { Text("Favorites") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate("profile")
                                },
                                text = { Text("Profile") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    menuExpanded = false
                                    handleLogout(navController)
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
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        movieViewModel.searchQuery = it
                    },
                    label = { Text("Search Movies") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            movieViewModel.searchMovies(searchQuery)
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }
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
                        val movieImage = movieImages[movie.id]
                        MovieItemCard(movie, configuration, movieImage, userProfile?.userName ?: "") {
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
}

private fun handleLogout(navController: NavController) {
    // Clear user session or any other logout logic
    navController.navigate("login_screen") {
        popUpTo("landing_screen") { inclusive = true }
    }
}

@Composable
fun MovieItemCard(movie: MovieResponse, configuration: ConfigurationResponse?, movieImages: MovieImagesResponse?, userName: String, onClick: () -> Unit) {
    val viewModel: MovieViewModel = viewModel()
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(movie.id) {
        isFavorite = viewModel.isFavoriteMovie(movie.id, userName)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        movieImages?.backdrops?.firstOrNull()?.file_path?.let { filePath ->
                            configuration?.images?.secure_base_url?.let { baseUrl ->
                                baseUrl + configuration.images.backdrop_sizes[1] + filePath
                            }
                        }
                    ),
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
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
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
                    }
                }
            }
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                tint = if (isFavorite) Color.Yellow else Color.Gray,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .clickable {
                        isFavorite = !isFavorite
                        if (isFavorite) {
                            viewModel.addFavoriteMovie(movie, userName, movieImages?.backdrops?.firstOrNull()?.file_path)
                        } else {
                            viewModel.removeFavoriteMovie(movie.id, userName)
                        }
                    }
            )
        }
    }
}

private fun preloadImage(context: android.content.Context, url: String) {
    val imageLoader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(url)
        .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
        .build()
    imageLoader.enqueue(request)
}