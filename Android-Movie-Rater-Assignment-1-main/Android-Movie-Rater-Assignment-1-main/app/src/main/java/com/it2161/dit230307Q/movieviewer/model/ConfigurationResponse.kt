package com.it2161.dit230307Q.movieviewer.model

data class ConfigurationResponse(
    val images: Images
)

data class Images(
    val base_url: String,
    val secure_base_url: String,
    val backdrop_sizes: List<String>,
    val logo_sizes: List<String>,
    val poster_sizes: List<String>,
    val profile_sizes: List<String>,
    val still_sizes: List<String>
)