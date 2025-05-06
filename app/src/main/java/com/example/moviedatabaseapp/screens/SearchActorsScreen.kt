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
import com.example.moviedatabaseapp.data.Movie
import com.example.moviedatabaseapp.data.MovieDao
import kotlinx.coroutines.launch

@Composable
fun SearchActorsScreen(movieDao: MovieDao, navController: NavController) {

    // State variables to manage actor name input, search results, and search button click

    var actorName by rememberSaveable { mutableStateOf("") }
    var searchResults by rememberSaveable { mutableStateOf<List<Movie>>(emptyList()) }
    var isSearchClicked by rememberSaveable { mutableStateOf(false) }
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
            Spacer(modifier = Modifier.height(30.dp))

            // Title for the screen
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SEARCH BY ACTOR",
                    fontSize = 30.sp,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Input field for actor name
            OutlinedTextField(
                value = actorName,
                onValueChange = { actorName = it },
                placeholder = { Text("Enter Actor Name") },
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

            // Search button to trigger actor search
            Button(
                onClick = {
                    scope.launch {
                        isSearchClicked = true
                        if (actorName.isNotBlank()) {

                            // Fetch movies by actor name from the database
                            movieDao.findMoviesByActor(actorName).collect { movies ->
                                searchResults = movies
                            }
                        } else {
                            Toast.makeText(context, "Please enter an actor's name", Toast.LENGTH_SHORT).show()
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

            Spacer(modifier = Modifier.height(20.dp))

            // Display search results or a message if no results are found
            if (isSearchClicked) {
                if (searchResults.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(searchResults) { movie ->
                                MovieItem(movie) // Display each movie in the results
                            }
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
                // Display the movie poster or a placeholder if the image is not available
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
                        fontSize = 25.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie.actors,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = movie.runtime + "  |  ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                        Text(
                            text = movie.year,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}