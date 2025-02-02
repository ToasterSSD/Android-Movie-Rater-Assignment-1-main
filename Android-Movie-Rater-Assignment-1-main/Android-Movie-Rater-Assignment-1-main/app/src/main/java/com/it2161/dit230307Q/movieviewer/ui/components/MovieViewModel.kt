package com.it2161.dit230307Q.movieviewer.ui.components

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.it2161.dit230307Q.movieviewer.MovieRaterApplication
import com.it2161.dit230307Q.movieviewer.data.FavoriteMovie
import com.it2161.dit230307Q.movieviewer.data.repository.FavoriteMovieRepository
import com.it2161.dit230307Q.movieviewer.data.repository.MovieRepository
import com.it2161.dit230307Q.movieviewer.model.ConfigurationResponse
import com.it2161.dit230307Q.movieviewer.model.MovieDetailResponse
import com.it2161.dit230307Q.movieviewer.model.MovieImagesResponse
import com.it2161.dit230307Q.movieviewer.model.MovieResponse
import com.it2161.dit230307Q.movieviewer.model.MovieReviewsResponse
import com.it2161.dit230307Q.movieviewer.model.Review
import com.it2161.dit230307Q.movieviewer.model.toResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel(application: Application, private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val repository = MovieRepository(application)
    private val favoriteMovieRepository: FavoriteMovieRepository
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _movies = MutableStateFlow<List<MovieResponse>>(emptyList())
    private val _movieImages = MutableStateFlow<Map<Int, MovieImagesResponse>>(emptyMap())
    private val _reviews = MutableStateFlow<MovieReviewsResponse?>(null)
    private val _favoriteMovies = MutableStateFlow<List<FavoriteMovie>>(emptyList())
    private val _similarMovies = MutableStateFlow<List<MovieResponse>>(emptyList())

    val similarMovies: StateFlow<List<MovieResponse>> = _similarMovies

    var selectedMovie: MovieResponse? by mutableStateOf(null)
        private set

    var selectedMovieDetails: MovieDetailResponse? by mutableStateOf(null)
        private set

    val movies: StateFlow<List<MovieResponse>> = _movies

    var configuration: ConfigurationResponse? by mutableStateOf(null)
        private set

    val movieImages: StateFlow<Map<Int, MovieImagesResponse>> = _movieImages

    val reviews: StateFlow<MovieReviewsResponse?> = _reviews

    var selectedReview: Review? by mutableStateOf(null)
        private set

    val favoriteMovies: StateFlow<List<FavoriteMovie>> = _favoriteMovies

    val errorMessage: StateFlow<String?> = _errorMessage

    var searchQuery: String
        get() = savedStateHandle.get<String>("searchQuery") ?: ""
        set(value) {
            savedStateHandle.set("searchQuery", value)
        }

    init {
        val favoriteMovieDao = (application as MovieRaterApplication).database.favoriteMovieDao()
        favoriteMovieRepository = FavoriteMovieRepository(favoriteMovieDao)
        fetchMovies("Popular")
        fetchConfiguration()
    }

    suspend fun isFavoriteMovie(movieId: Int, userName: String): Boolean {
        return favoriteMovieRepository.getFavoriteMovies(userName).any { it.movieId == movieId }
    }

    fun addFavoriteMovie(movie: MovieResponse, userName: String, file_path: String?) {
        viewModelScope.launch {
            val favoriteMovie = FavoriteMovie(
                movieId = movie.id,
                userName = userName,
                title = movie.title,
                overview = movie.overview,
                posterPath = movie.poster_path,
                voteAverage = movie.vote_average,
                file_path = file_path ?: ""
            )
            favoriteMovieRepository.insertFavoriteMovie(favoriteMovie)
            loadFavoriteMovies(userName)
        }
    }

    fun removeFavoriteMovie(movieId: Int, userName: String) {
        viewModelScope.launch {
            favoriteMovieRepository.deleteFavoriteMovie(movieId, userName)
            loadFavoriteMovies(userName)
        }
    }

    fun loadFavoriteMovies(userName: String) {
        viewModelScope.launch {
            _favoriteMovies.value = favoriteMovieRepository.getFavoriteMovies(userName)
        }
    }

    fun getMovieById(movieId: Int): MovieResponse? {
        return _movies.value.find { it.id == movieId }
    }

    fun loadMovie(movieTitle: String) {
        viewModelScope.launch {
            selectedMovie = _movies.value.firstOrNull { it.title == movieTitle }
        }
    }

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            selectedMovieDetails = repository.getMovieDetails(movieId)
        }
    }

    fun loadMovieReviews(movieId: Int) {
        viewModelScope.launch {
            _reviews.value = repository.getMovieReviews(movieId)
        }
    }

    fun loadReviewById(reviewId: String) {
        viewModelScope.launch {
            val reviewsResponse = reviews.value
            selectedReview = reviewsResponse?.results?.firstOrNull { it.id == reviewId }
        }
    }

    fun fetchMovies(category: String) {
        viewModelScope.launch {
            val response = try {
                val movies = when (category) {
                    "Popular" -> repository.getPopularMovies()
                    "Top Rated" -> repository.getTopRatedMovies()
                    "Now Playing" -> repository.getNowPlayingMovies()
                    "Upcoming" -> repository.getUpcomingMovies()
                    else -> repository.getPopularMovies()
                }
                repository.insertMovies(movies.map { it.toEntity(category) })
                movies
            } catch (e: Exception) {
                repository.getMoviesByCategory(category).map { it.toResponse() }
            }
            _movies.value = response
            response.forEach { movie ->
                fetchMovieImages(movie.id)
            }
        }
    }

    fun fetchConfiguration() {
        viewModelScope.launch {
            configuration = repository.getConfiguration()
        }
    }

    private fun fetchMovieImages(movieId: Int) {
        viewModelScope.launch {
            val imagesResponse = repository.getMovieImages(movieId)
            _movieImages.value = _movieImages.value + (movieId to imagesResponse)
        }
    }

    fun loadReviewById(movieId: Int, reviewId: String) {
        viewModelScope.launch {
            val reviewsResponse = repository.getMovieReviews(movieId)
            selectedReview = reviewsResponse.results.firstOrNull { it.id == reviewId }
        }
    }

    fun searchMovies(query: String) {
        if (query.isBlank()) {
            fetchMovies("Popular")
            return
        }
        searchQuery = query
        viewModelScope.launch {
            try {
                val response = repository.searchMovies(query)
                _movies.value = response
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch search results: ${e.message}"
            }
        }
    }

    fun loadSimilarMovies(movieId: Int) {
        viewModelScope.launch {
            try {
                _similarMovies.value = repository.getSimilarMovies(movieId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch similar movies: ${e.message}"
            }
        }
    }
}