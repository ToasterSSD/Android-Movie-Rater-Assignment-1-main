package com.it2161.dit230307Q.assignment1.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.it2161.dit230307Q.assignment1.MovieRaterApplication
import com.it2161.dit230307Q.assignment1.data.MovieItem

@ExperimentalMaterial3Api
@Composable
fun LandingScreen(navController: NavController) {
    val movieList = remember { mutableStateListOf<MovieItem>().apply { addAll(MovieRaterApplication.instance.data) } }
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "PopCornMovie", // Title of the app
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp), // Increased top padding to push title further down
                        textAlign = TextAlign.Center, // Ensures it's centered
                        color = Color.White // Ensure the text color is white
                    )
                },
                actions = {
                    // Overflow menu icon placed on the top-right
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White // Set the icon color to black
                        )
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
                modifier = Modifier
                    .height(120.dp) // Increased height of the top app bar to provide space
                    .padding(bottom = 4.dp), // Minor bottom padding to ensure content doesn't overlap
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White // Ensure the title color is white
                )
            )
        }
    ) { innerPadding -> // Use innerPadding from Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding) // Ensures content isn't hidden behind the TopAppBar
        ) {
            if (movieList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No movies found.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(movieList) { movie ->
                        MovieItemCard(movie = movie, onClick = {
                            navController.navigate("movieDetail/${movie.title}")
                        })
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
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Movie Thumbnail (Image) Section
            val bitmap: Bitmap = MovieRaterApplication.instance.getImgVector(movie.image)
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Thumbnail for ${movie.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)  // Adjusted size of image
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Movie Title, Synopsis, and Rating Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 14.dp)  // Padding to avoid text touching the right side
            ) {
                // Movie Title
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Movie Synopsis with Rating placed to the right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,  // Keeps the synopsis on the left
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Movie Synopsis
                    Text(
                        text = movie.synopsis,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,  // Add ellipsis for long texts
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.weight(10f) // Ensures synopsis takes more space
                    )

                    Spacer(modifier = Modifier.width(12.dp)) // Increased space between synopsis and rating

                    // Movie Rating
                    Text(
                        text = "${movie.ratings_score} /10 ⭐", // Displaying the rating
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}