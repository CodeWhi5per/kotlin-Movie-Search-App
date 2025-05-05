package com.example.moviedatabaseapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moviedatabaseapp.data.MovieApiResponse
import com.example.moviedatabaseapp.network.searchMoviesByTitle
import kotlinx.coroutines.launch

@Composable
fun SearchByTitleScreen(navController: NavController, initialQuery: String) {
    var query by rememberSaveable { mutableStateOf(initialQuery) }
    var searchResults by remember { mutableStateOf<List<MovieApiResponse>>(emptyList()) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(initialQuery) {
        if (initialQuery.isNotBlank()) {
            scope.launch {
                val movies = searchMoviesByTitle(initialQuery)
                searchResults = movies
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Enter Movie Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                scope.launch {
                    if (query.isNotBlank()) {
                        val movies = searchMoviesByTitle(query)
                        if (movies.isNotEmpty()) {
                            searchResults = movies
                        } else {
                            Toast.makeText(context, "No movies found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please enter a valid query", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Search")
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn {
                items(searchResults) { movie ->
                    MovieItem(movie)
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: MovieApiResponse) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    // Load the image asynchronously
    LaunchedEffect(movie.poster) {
        if (movie.poster.isNotBlank() && movie.poster != "N/A") {
            bitmap = loadImageFromUrl(movie.poster)
        }
    }

    Column(modifier = Modifier.padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Movie Poster",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(end = 8.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Icon",
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
            Column {
                Text("Title: ${movie.title}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                Text("Year: ${movie.year}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                Text("Runtime: ${movie.runtime}", style = MaterialTheme.typography.bodySmall, color = Color.White)
                Text("Genre: ${movie.genre}", style = MaterialTheme.typography.bodySmall, color = Color.White)
                Text("IMDb Rating: ${movie.imdbRating}", style = MaterialTheme.typography.bodySmall, color = Color.White)
            }
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray)
    }
}
