// com.example.techinterviewdemo.presentation.viewmodel.MoviesViewModel.kt
package com.example.techinterviewdemo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techinterviewdemo.domain.usecase.GetMoviesUseCase
import com.example.techinterviewdemo.presentation.state.MoviesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MoviesViewModel(private val getMoviesUseCase: GetMoviesUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<MoviesUiState>(MoviesUiState.Loading)
    val uiState: StateFlow<MoviesUiState> = _uiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = MoviesUiState.Loading

            getMoviesUseCase.getPopularMovies()
                .catch { e ->
                    _uiState.value = MoviesUiState.Error(e.message ?: "Unknown error")
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { movies ->
                            _uiState.value = MoviesUiState.Success(movies)
                        },
                        onFailure = { e ->
                            _uiState.value = MoviesUiState.Error(e.message ?: "Unknown error")
                        }
                    )
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                getMoviesUseCase.refreshMovies()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun toggleFavorite(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            getMoviesUseCase.toggleFavorite(movieId, isFavorite)
        }
    }
}