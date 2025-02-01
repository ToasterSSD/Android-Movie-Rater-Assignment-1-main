package com.it2161.dit230307Q.movieviewer.ui.components

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.it2161.dit230307Q.movieviewer.MovieRaterApplication
import com.it2161.dit230307Q.movieviewer.R
import com.it2161.dit230307Q.movieviewer.data.UserProfile
import com.it2161.dit230307Q.movieviewer.ui.theme.Assignment1Theme
import java.util.Calendar

@ExperimentalMaterial3Api
@Composable
fun EditProfileScreen(
    navController: NavController,
    onSave: () -> Unit,
    onBack: () -> Unit,
    viewModel: UserProfileViewModel
) {
    val currentProfile by viewModel.userProfile.collectAsState()

    var selectedAvatar by remember { mutableStateOf(currentProfile?.avatar ?: R.drawable.avatar_1) }
    var userName by remember { mutableStateOf(currentProfile?.userName ?: "") }
    var email by remember { mutableStateOf(currentProfile?.email ?: "") }
    var gender by remember { mutableStateOf(currentProfile?.gender ?: "") }
    var mobile by remember { mutableStateOf(currentProfile?.mobile ?: "") }
    var yob by remember { mutableStateOf(currentProfile?.yob ?: "") }
    var updates by remember { mutableStateOf(currentProfile?.updates ?: false) }

    // State for errors
    var isEmailInvalid by remember { mutableStateOf(false) }
    var isUserNameEmpty by remember { mutableStateOf(false) }
    var isMobileEmpty by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Profile",
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
                    Button(
                        onClick = {
                            // Validate fields
                            if (userName.isEmpty() || email.isEmpty() || mobile.isEmpty() || yob.isEmpty()) {
                                Toast.makeText(navController.context, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
                            } else if (isEmailInvalid) {
                                Toast.makeText(navController.context, "Email must contain '@'", Toast.LENGTH_SHORT).show()
                            } else {
                                // Save the user profile if validation passes
                                val updatedProfile = UserProfile(
                                    userName = userName,
                                    email = email,
                                    gender = gender,
                                    mobile = mobile,
                                    yob = yob,
                                    updates = updates,
                                    avatar = selectedAvatar // Save selected avatar
                                )
                                viewModel.updateUserProfile(updatedProfile)
                                MovieRaterApplication.instance.userProfile = updatedProfile
                                onSave()
                            }
                        },
                        modifier = Modifier.size(100.dp, 40.dp) // Set the size of the button
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                        Spacer(modifier = Modifier.width(4.dp)) // Adjust spacing if needed
                        Text("Save", fontSize = 12.sp) // Adjust text size if needed
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                AvatarSelection(selectedAvatar) { selectedAvatar = it }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = {
                    userName = it
                    isUserNameEmpty = userName.isEmpty() // Check if username is empty
                },
                label = { Text("User Name") },
                singleLine = true,
                isError = isUserNameEmpty, // Show error if empty
                modifier = Modifier.fillMaxWidth()
            )
            if (isUserNameEmpty) {
                Text("Please fill up", color = Color.Red, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    isEmailInvalid = !email.contains("@") // Check if email contains "@"
                },
                label = { Text("Email") },
                singleLine = true,
                isError = isEmailInvalid, // Show error if invalid email
                modifier = Modifier.fillMaxWidth()
            )
            if (isEmailInvalid) {
                Text("Email must contain '@'", color = Color.Red, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(16.dp))

            GenderSelection(gender) { gender = it }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = mobile,
                onValueChange = { newValue ->
                    // Allow only digits in the mobile number field
                    if (newValue.all { it.isDigit() }) {
                        mobile = newValue
                    }
                    isMobileEmpty = mobile.isEmpty() // Check if mobile is empty
                },
                label = { Text("Mobile") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isMobileEmpty, // Show error if mobile is empty
                modifier = Modifier.fillMaxWidth()
            )
            if (isMobileEmpty) {
                Text("Please fill up", color = Color.Red, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(16.dp))

            YearOfBirthDropdown(yob) { yob = it }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = updates,
                    onCheckedChange = { updates = it }
                )
                Text("Receive Updates")
            }
        }
    }
}

@Composable
fun AvatarSelection(selectedAvatar: Int, onAvatarSelected: (Int) -> Unit) {
    val avatars = listOf(
        R.drawable.avatar_1,
        R.drawable.avatar_2,
        R.drawable.avatar_3,
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        avatars.forEach { avatar ->
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = if (avatar == selectedAvatar) Color.Blue else Color.Gray,
                        shape = CircleShape
                    )
                    .background(if (avatar == selectedAvatar) Color.LightGray else Color.Transparent)
                    .clickable { onAvatarSelected(avatar) }
            ) {
                Image(
                    painter = painterResource(id = avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun GenderSelection(selectedGender: String, onGenderSelected: (String) -> Unit) {
    val genders = listOf("Male", "Female", "Non-Binary", "Prefer not to say")

    Column {
        genders.forEach { gender ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onGenderSelected(gender) }
            ) {
                RadioButton(
                    selected = gender == selectedGender,
                    onClick = { onGenderSelected(gender) }
                )
                Text(text = gender, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun YearOfBirthDropdown(selectedYear: String, onYearSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (1920..currentYear).map { it.toString() }

    Box {
        OutlinedTextField(
            value = selectedYear,
            onValueChange = { onYearSelected(it) },
            label = { Text("Year of Birth") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
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
                        onYearSelected(year)
                        expanded = false
                    }
                )
            }
        }
    }
}