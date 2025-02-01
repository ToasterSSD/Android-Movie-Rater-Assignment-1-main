package com.it2161.dit230307Q.movieviewer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.it2161.dit230307Q.movieviewer.R
import com.it2161.dit230307Q.movieviewer.data.UserProfile

@ExperimentalMaterial3Api
@Composable
fun ProfileScreen(
    navController: NavController,
    onBack: () -> Unit,
    viewModel: UserProfileViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    var menuExpanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit Profile") },
                            onClick = {
                                menuExpanded = false
                                navController.navigate("edit_profile_screen")
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        userProfile?.let { profile ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar Section - Display selected avatar
                Image(
                    painter = painterResource(id = profile.avatar),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // User Details Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RectangleShape)
                        .padding(16.dp)
                ) {
                    Column {
                        ProfileDetailItem(label = "UserName", value = profile.userName)
                        ProfileDetailItem(label = "Email", value = profile.email)
                        ProfileDetailItem(label = "Gender", value = profile.gender)
                        ProfileDetailItem(label = "Mobile", value = profile.mobile)
                        ProfileDetailItem(label = "Year of Birth", value = profile.yob)
                        ProfileDetailItem(label = "Receive Updates", value = if (profile.updates) "Yes" else "No")
                    }
                }
            }
        } ?: run {
            // Handle the case where userProfile is null
            Text("Loading...", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun ProfileDetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 16.sp,
        )
    }
}