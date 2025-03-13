// com.example.techinterviewdemo.data.repository.MovieRepositoryImpl.kt
package com.example.techinterviewdemo.data.repository

import com.example.techinterviewdemo.data.api.MovieApiService
import com.example.techinterviewdemo.data.db.MovieDao
import com.example.techinterviewdemo.data.db.MovieEntity
import com.example.techinterviewdemo.data.model.MovieDto
import com.example.techinterviewdemo.domain.model.Movie
import com.example.techinterviewdemo.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class MovieRepositoryImpl(
    private val movieApiService: MovieApiService,
    private val movieDao: MovieDao
) : MovieRepository {

    private val apiKey = "your_api_key" // En un caso real, esto vendría de BuildConfig

    override fun getPopularMovies(): Flow<Result<List<Movie>>> {
        return movieDao.getAllMovies().map { entities ->
            Result.success(entities.map { it.toDomainModel() })
        }
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return movieDao.getFavoriteMovies().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun toggleFavorite(movieId: Int, isFavorite: Boolean) {
        movieDao.updateFavoriteStatus(movieId, isFavorite)
    }

    override suspend fun refreshMovies() {
        try {
            val response = movieApiService.getPopularMovies(apiKey)
            val movieEntities = response.results.map { it.toEntity() }
            movieDao.insertMovies(movieEntities)
        } catch (e: IOException) {
            // En un caso real, manejaríamos esto con un Result.failure()
        }
    }

    // Funciones de mapeo
    private fun MovieDto.toEntity(): MovieEntity {
        return MovieEntity(
            id = id,
            title = title,
            posterPath = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
            backdropPath = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" },
            overview = overview,
            rating = rating,
            releaseDate = releaseDate,
            isFavorite = false // Por defecto no es favorita
        )
    }

    private fun MovieEntity.toDomainModel(): Movie {
        return Movie(
            id = id,
            title = title,
            posterUrl = posterPath,
            backdropUrl = backdropPath,
            overview = overview,
            rating = rating,
            releaseDate = releaseDate,
            isFavorite = isFavorite
        )
    }
}
