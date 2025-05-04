package com.example.moviedatabaseapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moviedatabaseapp.data.MovieApiResponse
import com.example.moviedatabaseapp.network.searchMoviesByTitle
import kotlinx.coroutines.launch

@Composable
fun SearchByTitleScreen(navController: NavController) {
    var query by rememberSaveable { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<MovieApiResponse>>(emptyList()) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
    Column(modifier = Modifier.padding(8.dp)) {
        Text("Title: ${movie.title}", style = MaterialTheme.typography.bodyLarge)
        Text("Year: ${movie.year}", style = MaterialTheme.typography.bodyMedium)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}