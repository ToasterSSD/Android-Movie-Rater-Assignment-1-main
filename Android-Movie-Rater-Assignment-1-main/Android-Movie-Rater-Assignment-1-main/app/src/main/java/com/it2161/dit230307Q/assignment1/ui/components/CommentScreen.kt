package com.it2161.dit230307Q.assignment1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.it2161.dit230307Q.assignment1.data.Comments
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@ExperimentalMaterial3Api
@Composable
fun CommentMovieScreen(
    navController: NavController,
    movieTitle: String?,
    commentUser: String?,
    commentText: String?,
    commentDate: String?,
    commentTime: String?,
    viewModel: MovieViewModel = viewModel()
) {
    // Load movie details based on movieTitle
    LaunchedEffect(movieTitle) {
        viewModel.loadMovie(movieTitle ?: "")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = movieTitle ?: "Comment",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                modifier = Modifier.height(120.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Enable scrolling if content overflows
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (commentUser != null && commentText != null && commentDate != null && commentTime != null) {
                val comment = Comments(commentUser, commentText, commentDate, commentTime)
                CommentItem(comment = comment, movieTitle = movieTitle ?: "", navController = navController, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comments, movieTitle: String, navController: NavController, viewModel: MovieViewModel) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val commentDateTime = LocalDateTime.parse("${comment.date} ${comment.time}", formatter)
    val currentTime = LocalDateTime.now()
    val durationMinutes = ChronoUnit.MINUTES.between(commentDateTime, currentTime)

    val timeAgo = when {
        durationMinutes < 1 -> "Just Now"
        durationMinutes < 60 -> "$durationMinutes min ago"
        durationMinutes < 1440 -> "${durationMinutes / 60} hrs ago"
        durationMinutes < 43200 -> "${durationMinutes / 1440} days ago"
        durationMinutes < 525600 -> {
            val months = durationMinutes / 43200
            val remainingDays = (durationMinutes % 43200) / 1440
            if (remainingDays > 0) "$months months $remainingDays days ago" else "$months months ago"
        }
        else -> {
            val years = durationMinutes / 525600
            val remainingMonths = (durationMinutes % 525600) / 43200
            if (remainingMonths > 0) "$years year(s) $remainingMonths month(s) ago" else "$years year(s) ago"
        }
    }

    val initials = comment.user.split(Regex("(?=[A-Z])"))
        .filter { it.isNotEmpty() }
        .joinToString(".") { it.firstOrNull()?.toString()?.uppercase() ?: "" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Color.Gray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(text = comment.user, style = MaterialTheme.typography.titleSmall)
                Text(text = timeAgo, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(32.dp)) // Increased spacing

        Text(
            text = comment.comment,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth() // Make the comment text take full width
        )
    }
}