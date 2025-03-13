// com.example.techinterviewdemo.domain.repository.MovieRepository.kt
package com.example.techinterviewdemo.domain.repository

import com.example.techinterviewdemo.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMovies(): Flow<Result<List<Movie>>>
    fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun toggleFavorite(movieId: Int, isFavorite: Boolean)
    suspend fun refreshMovies()
}