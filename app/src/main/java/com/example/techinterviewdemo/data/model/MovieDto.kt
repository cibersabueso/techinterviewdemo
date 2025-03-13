// com.example.techinterviewdemo.data.model.MovieDto.kt
package com.example.techinterviewdemo.data.model

import com.google.gson.annotations.SerializedName

data class MovieDto(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    val overview: String,
    @SerializedName("vote_average") val rating: Double,
    @SerializedName("release_date") val releaseDate: String
)

data class MovieResponse(
    val page: Int,
    val results: List<MovieDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)