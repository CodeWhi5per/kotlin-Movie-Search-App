package com.example.moviedatabaseapp.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.moviedatabaseapp.data.Movie
import com.example.moviedatabaseapp.data.MovieApiResponse
import com.example.moviedatabaseapp.data.MovieDao
import com.example.moviedatabaseapp.network.getMovieFromApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun SearchMovieScreen(movieDao: MovieDao) {
    var title by rememberSaveable { mutableStateOf("") }
    var movieResult by remember { mutableStateOf<MovieApiResponse?>(null) }
    var posterBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Movie Title") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                scope.launch {
                    val movie = getMovieFromApi(title)
                    if (movie != null) {
                        movieResult = movie
                        posterBitmap = loadImageFromUrl(movie.poster)
                    } else {
                        Toast.makeText(context, "Movie not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Retrieve Movie")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    movieResult?.let {
                        scope.launch {
                            val movieEntity = Movie(
                                imdbID = it.imdbID,
                                title = it.title,
                                year = it.year,
                                rated = it.rated,
                                released = it.released,
                                runtime = it.runtime,
                                genre = it.genre,
                                director = it.director,
                                writer = it.writer,
                                actors = it.actors,
                                plot = it.plot,
                                poster = it.poster
                            )
                            movieDao.insertAll(listOf(movieEntity))
                            Toast.makeText(context, "Movie saved to DB", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = movieResult != null
            ) {
                Text("Save movie to Database")
            }

            Spacer(modifier = Modifier.height(20.dp))

            movieResult?.let {
                posterBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Movie Poster",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("Title: ${it.title}", color = Color.White)
                Text("Year: ${it.year}", color = Color.White)
                Text("Rated: ${it.rated}", color = Color.White)
                Text("Released: ${it.released}", color = Color.White)
                Text("Runtime: ${it.runtime}", color = Color.White)
                Text("Genre: ${it.genre}", color = Color.White)
                Text("Director: ${it.director}", color = Color.White)
                Text("Writer: ${it.writer}", color = Color.White)
                Text("Actors: ${it.actors}", color = Color.White)
                Text("Plot: ${it.plot}", color = Color.White)
            }
        }
    }
}
suspend fun loadImageFromUrl(url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = URL(url).openStream()
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}