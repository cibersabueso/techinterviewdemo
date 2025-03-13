// com.example.techinterviewdemo.domain.usecase.GetMoviesUseCase.kt
package com.example.techinterviewdemo.domain.usecase

import com.example.techinterviewdemo.domain.model.Movie
import com.example.techinterviewdemo.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesUseCase(private val repository: MovieRepository) {

    fun getPopularMovies(): Flow<Result<List<Movie>>> {
        return repository.getPopularMovies()
    }

    fun getFavoriteMovies(): Flow<List<Movie>> {
        return repository.getFavoriteMovies()
    }

    suspend fun refreshMovies() {
        repository.refreshMovies()
    }

    suspend fun toggleFavorite(movieId: Int, isFavorite: Boolean) {
        repository.toggleFavorite(movieId, isFavorite)
    }
}