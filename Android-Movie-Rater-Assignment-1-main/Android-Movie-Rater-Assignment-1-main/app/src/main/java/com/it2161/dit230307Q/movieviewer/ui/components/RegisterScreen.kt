package com.it2161.dit230307Q.movieviewer.ui.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.it2161.dit230307Q.movieviewer.MovieRaterApplication
import com.it2161.dit230307Q.movieviewer.data.UserProfile
import java.util.Calendar

@Composable
fun RegisterUserScreen(
    navController: NavController,
    onRegisterSuccess: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var updates by remember { mutableStateOf(false) }
    var yob by remember { mutableStateOf("") }

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (1920..currentYear).map { it.toString() }

    // State variables for validation
    var isUserNameEmpty by remember { mutableStateOf(false) }
    var isPasswordEmpty by remember { mutableStateOf(false) }
    var isConfirmPasswordEmpty by remember { mutableStateOf(false) }
    var isEmailEmpty by remember { mutableStateOf(false) }
    var isEmailInvalid by remember { mutableStateOf(false) }
    var isGenderEmpty by remember { mutableStateOf(false) }
    var isMobileEmpty by remember { mutableStateOf(false) }
    var isYobEmpty by remember { mutableStateOf(false) }
    var isPasswordMismatch by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Registration Input Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 36.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Username Field
            OutlinedTextField(
                value = userName,
                onValueChange = {
                    userName = it
                    isUserNameEmpty = userName.isEmpty()
                },
                label = { Text("Enter User Name") },
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                },
                isError = isUserNameEmpty,
                modifier = Modifier.fillMaxWidth()
            )
            if (isUserNameEmpty) {
                Text("Please fill up", color = Color.Red, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isPasswordEmpty = password.isEmpty()
                    isPasswordMismatch = password != confirmPassword && confirmPassword.isNotEmpty()
                },
                label = { Text("Enter Password") },
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                isError = isPasswordEmpty,
                modifier = Modifier.fillMaxWidth()
            )
            if (isPasswordEmpty) {
                Text("Please fill up", color = Color.Red, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    isConfirmPasswordEmpty = confirmPassword.isEmpty()
                    isPasswordMismatch = password != confirmPassword
                },
                label = { Text("Enter Password") },
                singleLine = true,
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        val icon = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                isError = isConfirmPasswordEmpty || isPasswordMismatch,
                modifier = Modifier.fillMaxWidth()
            )
            if (isConfirmPasswordEmpty) {
                Text("Please fill up", color = Color.Red, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            } else if (isPasswordMismatch) {
                Text("Passwords do not match", color = Color.Red, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Email Field with validation
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    isEmailEmpty = email.isEmpty()
                    isEmailInvalid = !email.contains("@")
                },
                label = { Text("Enter Email") },
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = isEmailEmpty || isEmailInvalid,
                modifier = Modifier.fillMaxWidth()
            )
            if (isEmailEmpty) {
                Text("Please fill up", color = Color.Red, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            } else if (isEmailInvalid) {
                Text("Email must contain '@'", color = Color.Red, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Gender Selection
            Text(
                text = "Gender",
                color = if (isGenderEmpty) Color.Red else Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                listOf("Male", "Female", "Non-Binary", "Prefer not to say").forEach { option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == option,
                            onClick = { gender = option; isGenderEmpty = false }
                        )
                        Text(option, modifier = Modifier.padding(end = 8.dp))
                    }
                }
            }
            if (isGenderEmpty) {
                Text(
                    "Please select a gender",
                    color = Color.Red,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mobile Number Field
            OutlinedTextField(
                value = mobile,
                onValueChange = {
                    if (it.matches(Regex("^[0-9]*$"))) {
                        mobile = it
                        isMobileEmpty = mobile.isEmpty()
                    }
                },
                label = { Text("Enter Mobile Number") },
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                isError = isMobileEmpty,
                modifier = Modifier.fillMaxWidth()
            )
            if (isMobileEmpty) {
                Text("Please fill up", color = Color.Red, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Year of Birth Dropdown
            var expanded by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = yob,
                    onValueChange = {},
                    label = { Text("Select Year of Birth") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    isError = isYobEmpty
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    years.forEach { year ->
                        DropdownMenuItem(
                            text = { Text(year) },
                            onClick = {
                                yob = year
                                isYobEmpty = false
                                expanded = false
                            }
                        )
                    }
                }
            }
            if (isYobEmpty) {
                Text("Please select a year", color = Color.Red, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = updates, onCheckedChange = { updates = it })
                Text(text = "Receive updates via email")
            }
        }

        // Registration Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                isUserNameEmpty = userName.isEmpty()
                isPasswordEmpty = password.isEmpty()
                isConfirmPasswordEmpty = confirmPassword.isEmpty()
                isEmailEmpty = email.isEmpty()
                isGenderEmpty = gender.isEmpty()
                isMobileEmpty = mobile.isEmpty()
                isYobEmpty = yob.isEmpty()
                isPasswordMismatch = password != confirmPassword

                if (isUserNameEmpty || isPasswordEmpty || isConfirmPasswordEmpty ||
                    isEmailEmpty || isGenderEmpty || isMobileEmpty || isYobEmpty || isPasswordMismatch) {
                    Toast.makeText(context, "Please fill out all required fields", Toast.LENGTH_SHORT).show()
                } else {
                    MovieRaterApplication.instance.userProfile = UserProfile(
                        userName = userName,
                        password = password,
                        email = email,
                        gender = gender,
                        mobile = mobile,
                        updates = updates,
                        yob = yob
                    )
                    Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                    onRegisterSuccess()
                    navController.navigate("profile")
                }
            }) {
                Text("Register")
            }

            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}