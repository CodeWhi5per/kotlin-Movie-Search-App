package com.example.moviedatabaseapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moviedatabaseapp.data.MovieApiResponse
import com.example.moviedatabaseapp.network.searchMoviesByTitle
import kotlinx.coroutines.launch

@Composable
fun SearchByTitleScreen(navController: NavController, initialQuery: String) {

    // State variables to manage the search query and results
    var query by rememberSaveable { mutableStateOf(initialQuery) }
    var searchResults by rememberSaveable { mutableStateOf<List<MovieApiResponse>>(emptyList()) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Automatically trigger search if an initial query is provided
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

            Spacer(modifier = Modifier.height(30.dp))

            // Title of the screen
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DISCOVER MOVIES",
                    fontSize = 30.sp,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Search input field
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Enter Movie Title") },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                        colors = TextFieldDefaults.colors(
                            unfocusedTextColor = Color.Gray,
                            focusedTextColor = Color.Gray,
                            focusedContainerColor = Color(0xFF212121),
                            unfocusedContainerColor = Color(0xFF212121),
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .width(350.dp)
                            .height(56.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Search button
                    Button(
                        onClick = {
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
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF009DFF),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .width(150.dp)
                            .height(50.dp)
                    ) {
                        Text("Search", fontSize = 15.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Display search results in a list
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

    // Load the movie poster image asynchronously
    LaunchedEffect(movie.poster) {
        if (movie.poster.isNotBlank() && movie.poster != "N/A") {
            bitmap = loadImageFromUrl(movie.poster)
        }
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A1A)),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display the movie poster or a placeholder if unavailable
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
                            .padding(end = 8.dp)
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
                // Display movie details
                Column {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Runtime: ${movie.runtime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                    Text(
                        text = "Director: ${movie.director}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = movie.year,
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star Icon",
                            tint = Color(0xFFFFB700),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = movie.imdbRating,
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}