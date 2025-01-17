
package com.it2161.dit230307Q.movieviewer.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.it2161.dit230307Q.movieviewer.MovieRaterApplication
import com.it2161.dit230307Q.movieviewer.data.Comments
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@ExperimentalMaterial3Api
@Composable
fun MovieDetailScreen(
    movieTitle: String?,
    navController: NavController,
    viewModel: MovieViewModel = viewModel()
) {
    LaunchedEffect(movieTitle) {
        viewModel.loadMovie(movieTitle ?: "")
    }

    val movie = viewModel.selectedMovie

    if (movie != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = movie.title,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate("landing_screen") }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        var menuExpanded by remember { mutableStateOf(false) }
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    menuExpanded = false
                                    navController.navigate("add_comment_screen/${movie.title}")
                                },
                                text = { Text("Add Comments") }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val bitmap: Bitmap = MovieRaterApplication.instance.getImgVector(movie.image)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Poster for ${movie.title}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Director: ${movie.director}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Release Date: ${movie.releaseDate}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Genre: ${movie.genre}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Length: ${movie.length} mins", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Rating: ${movie.ratings_score} ⭐", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Actors: ${movie.actors.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Synopsis:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = movie.synopsis,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 2.dp,
                    color = Color.Black
                )

                Text(
                    text = "Comments",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )

                val sortedComments = movie.comment.sortedByDescending { it.date + it.time }
                sortedComments.forEach { comment ->
                    CommentItem(
                        comment = comment,
                        movieTitle = movie.title,
                        navController = navController,
                        viewModel = viewModel,
                        onClick = {
                            navController.navigate("comment_screen/${movie.title}/${comment.user}/${comment.comment}/${comment.date}/${comment.time}")
                        }
                    )
                }
            }
        }
    } else {
        Text(text = "Movie not found", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
    }
}

@Composable
fun CommentItem(comment: Comments, movieTitle: String, navController: NavController, viewModel: MovieViewModel, onClick: () -> Unit = {}) {
    val timeAgo = getTimeAgo(comment.date, comment.time)

    val initials = comment.user.split(Regex("(?=[A-Z])"))
        .filter { it.isNotEmpty() }
        .joinToString(".") { it.firstOrNull()?.toString()?.uppercase() ?: "" }

    // Comment item layout
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        // Row to display the circle, user's name, and time ago
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Circle with user initials
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.LightGray, Color.DarkGray)
                        )
                    )
                    .border(1.dp, Color.Gray, CircleShape)
                    .shadow(4.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(1.dp))
            // Comment user's name (Bold)
            Text(
                text = comment.user,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 50.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            // Display the time ago in small, gray text at the top right
            Text(
                text = timeAgo,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Spacer(modifier = Modifier.width(8.dp))

        // Display the comment text
        Text(
            text = comment.comment,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 50.dp)
        )
    }
}

// Updated function to calculate time ago with months and years
fun getTimeAgo(date: String, time: String): String {
    return try {
        val dateTimeString = "$date $time"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val commentDate: Date = dateFormat.parse(dateTimeString) ?: return "Unknown time"

        val now = Date()
        val diffInMillis = now.time - commentDate.time

        // Convert milliseconds into seconds, minutes, hours, days, months, and years
        val seconds = diffInMillis / 1000
        val minutes = diffInMillis / (1000 * 60)
        val hours = diffInMillis / (1000 * 60 * 60)
        val days = diffInMillis / (1000 * 60 * 60 * 24)
        val months = days / 30
        val years = days / 365

        // Calculate remaining months and days after calculating years
        val remainingDays = days % 365
        val remainingMonths = remainingDays / 30
        val remainingFinalDays = remainingDays % 30

        // Display the appropriate time format
        when {
            years > 0 -> "$years year${if (years > 1) "s" else ""} ${if (remainingMonths > 0) "$remainingMonths month${if (remainingMonths > 1) "s" else ""}" else ""}".trim()
            months > 0 -> "$months month${if (months > 1) "s" else ""} ${if (remainingFinalDays > 0) "$remainingFinalDays day${if (remainingFinalDays > 1) "s" else ""}" else ""}".trim()
            days > 0 -> "$remainingFinalDays day${if (remainingFinalDays > 1) "s" else ""} ago"
            hours > 0 -> "$hours hr${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "$minutes min${if (minutes > 1) "s" else ""} ago"
            else -> "$seconds sec${if (seconds > 1) "s" else ""} ago"
        }
    } catch (e: Exception) {
        "Unknown time"
    }
}