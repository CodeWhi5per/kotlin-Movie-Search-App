package com.example.moviedatabaseapp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moviedatabaseapp.data.Movie
import com.example.moviedatabaseapp.data.MovieDao
import kotlinx.coroutines.launch

@Composable
fun SearchActorsScreen(movieDao: MovieDao, navController: NavController) {
    var actorName by rememberSaveable { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isSearchClicked by remember { mutableStateOf(false) }
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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = actorName,
                onValueChange = { actorName = it },
                label = { Text("Actor Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                scope.launch {
                    isSearchClicked = true
                    if (actorName.isNotBlank()) {
                        movieDao.findMoviesByActor(actorName).collect { movies ->
                            searchResults = movies
                        }
                    } else {
                        Toast.makeText(context, "Please enter an actor's name", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Search")
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isSearchClicked) {
                if (searchResults.isNotEmpty()) {
                    LazyColumn {
                        items(searchResults) { movie ->
                            MovieItem(movie)
                        }
                    }
                } else {
                    Text("No movies found", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text("Title: ${movie.title}", style = MaterialTheme.typography.bodyLarge)
        Text("Year: ${movie.year}", style = MaterialTheme.typography.bodyMedium)
        Text("Actors: ${movie.actors}", style = MaterialTheme.typography.bodySmall)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}