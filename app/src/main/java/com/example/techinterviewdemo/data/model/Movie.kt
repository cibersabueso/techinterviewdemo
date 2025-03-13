// com.example.techinterviewdemo.domain.model.Movie.kt
package com.example.techinterviewdemo.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val overview: String,
    val rating: Double,
    val releaseDate: String,
    val isFavorite: Boolean = false
)