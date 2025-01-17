
package com.it2161.dit230307Q.movieviewer.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.it2161.dit230307Q.movieviewer.MovieRaterApplication
import com.it2161.dit230307Q.movieviewer.data.Comments
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@ExperimentalMaterial3Api
@Composable
fun AddCommentScreen(navController: NavController, movieTitle: String) {
    val context = LocalContext.current
    val app = context.applicationContext as MovieRaterApplication
    val movie = app.data.find { it.title == movieTitle }
    val currentUser = app.userProfile

    if (movie == null || currentUser == null) {
        Text("Error: Movie or user not found.", style = MaterialTheme.typography.bodyLarge)
    } else {
        var commentText by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 90.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    )
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val bitmap: Bitmap? = movie.image.let { app.getImgVector(it) }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = movie.title,
                            modifier = Modifier.size(180.dp)
                        )
                    }

                    Text(
                        text = "Add Comments",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        label = { Text("Your Comment") },
                        placeholder = { Text("Enter your comment") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Button(
                        onClick = {
                            if (commentText.isBlank()) {
                                errorMessage = "Comment field is required."
                            } else {
                                val customTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                                val currentTime = LocalDateTime.now()
                                val date = currentTime.format(DateTimeFormatter.ISO_DATE)
                                val time = currentTime.format(customTimeFormatter)

                                val newComment = Comments(
                                    user = currentUser.userName,
                                    date = date,
                                    time = time,
                                    comment = commentText
                                )

                                val updatedComments = movie.comment.toMutableList()
                                updatedComments.add(0, newComment) // Add new comment at the top
                                movie.comment = updatedComments

                                app.data[app.data.indexOf(movie)] = movie // Save updated movie data
                                navController.popBackStack() // Return to MovieDetailScreen
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Submit", fontSize = 16.sp)
                    }
                }
            }
        )
    }
}