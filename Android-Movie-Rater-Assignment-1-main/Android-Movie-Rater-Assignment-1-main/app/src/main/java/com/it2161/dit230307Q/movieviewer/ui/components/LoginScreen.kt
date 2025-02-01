package com.it2161.dit230307Q.movieviewer.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.it2161.dit230307Q.movieviewer.MovieRaterApplication
import com.it2161.dit230307Q.movieviewer.R

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circular image
        Image(
            painter = painterResource(id = R.drawable.movie_viewer_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape) // Apply circle clipping
        )

        Spacer(modifier = Modifier.height(90.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("User ID") },
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field with visibility toggle using Material UI icons
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        handleLogin(userId, password, context, onLoginSuccess)
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    handleLogin(userId, password, context, onLoginSuccess)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("Register", color = Color.Blue)
            }
        }
    }
}

private fun handleLogin(
    userId: String,
    password: String,
    context: Context,
    onLoginSuccess: () -> Unit
) {
    val appInstance = MovieRaterApplication.instance
    val savedProfile = appInstance.userProfile

    if (userId.isEmpty() || password.isEmpty()) {
        Toast.makeText(context, "Please enter both User ID and Password", Toast.LENGTH_SHORT).show()
        return
    }

    if (savedProfile != null && savedProfile.userName == userId && savedProfile.password == password) {
        Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
        onLoginSuccess()
    } else if (userId == "TestUser1" && password == "TestPassword1") {
        Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
        onLoginSuccess()
    } else {
        Toast.makeText(context, "Invalid credentials!", Toast.LENGTH_SHORT).show()
    }
}