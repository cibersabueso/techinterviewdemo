// com.example.techinterviewdemo.data.db.MovieEntity.kt
package com.example.techinterviewdemo.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val overview: String,
    val rating: Double,
    val releaseDate: String,
    val isFavorite: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)