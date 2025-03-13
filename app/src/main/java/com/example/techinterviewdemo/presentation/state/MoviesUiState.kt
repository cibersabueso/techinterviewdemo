// com.example.techinterviewdemo.presentation.state.MoviesUiState.kt
package com.example.techinterviewdemo.presentation.state

import com.example.techinterviewdemo.domain.model.Movie

sealed class MoviesUiState {
    data object Loading : MoviesUiState()
    data class Success(val movies: List<Movie>) : MoviesUiState()
    data class Error(val message: String) : MoviesUiState()
}