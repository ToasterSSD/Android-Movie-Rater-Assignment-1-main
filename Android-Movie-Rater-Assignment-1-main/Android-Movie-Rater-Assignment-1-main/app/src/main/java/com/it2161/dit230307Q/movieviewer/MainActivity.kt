package com.it2161.dit230307Q.movieviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.it2161.dit230307Q.movieviewer.ui.theme.Assignment1Theme

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val application = application
        setContent {
            Assignment1Theme {
                MovieViewerApp(application)
            }
        }
    }
}