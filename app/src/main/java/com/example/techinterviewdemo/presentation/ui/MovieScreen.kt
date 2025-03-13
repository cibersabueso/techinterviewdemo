// com.example.techinterviewdemo.presentation.ui.MovieScreen.kt
package com.example.techinterviewdemo.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.techinterviewdemo.domain.model.Movie
import com.example.techinterviewdemo.presentation.state.MoviesUiState
import com.example.techinterviewdemo.presentation.viewmodel.MoviesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MovieScreen(
    viewModel: MoviesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refresh() }
    ) {
        when (val state = uiState) {
            is MoviesUiState.Loading -> LoadingScreen()
            is MoviesUiState.Success -> MovieList(
                movies = state.movies,
                onMovieClick = { /* Navegación a detalles, implementar luego */ },
                onFavoriteClick = { movie, isFavorite ->
                    viewModel.toggleFavorite(movie.id, isFavorite)
                }
            )
            is MoviesUiState.Error -> ErrorScreen(
                message = state.message,
                onRetry = { viewModel.loadMovies() }
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun MovieList(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    onFavoriteClick: (Movie, Boolean) -> Unit
) {
    LazyColumn {
        items(movies) { movie ->
            MovieItem(
                movie = movie,
                onMovieClick = onMovieClick,
                onFavoriteClick = onFavoriteClick
            )
            Divider()
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onMovieClick: (Movie) -> Unit,
    onFavoriteClick: (Movie, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMovieClick(movie) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Poster de la película
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            modifier = Modifier
                .width(80.dp)
                .height(120.dp),
            contentScale = ContentScale.Crop
        )

        // Información de la película
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "⭐ ${movie.rating}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = movie.releaseDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        // Botón de favorito
        IconButton(
            onClick = { onFavoriteClick(movie, !movie.isFavorite) }
        ) {
            Icon(
                imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (movie.isFavorite) "Remove from favorites" else "Add to favorites",
                tint = if (movie.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}