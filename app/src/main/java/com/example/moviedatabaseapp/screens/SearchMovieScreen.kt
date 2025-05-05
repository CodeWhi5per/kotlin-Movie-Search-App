package com.example.moviedatabaseapp.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
    var posterBitmap by remember { mutableStateOf<Bitmap?>(null) }
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
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SEARCH MOVIE",
                    fontSize = 30.sp,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
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

                    Button(
                        onClick = {
                            scope.launch {
                                val movie = getMovieFromApi(title)
                                if (movie != null) {
                                    movieResult = movie
                                    posterBitmap = loadImageFromUrl(movie.poster)
                                } else {
                                    Toast.makeText(context, "Movie not found", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF009DFF),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .width(200.dp)
                            .height(50.dp)
                    ) {
                        Text("RETRIEVE MOVIE", fontSize = 15.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            movieResult?.let {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1A1A)),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Column(modifier = Modifier.padding(15.dp)) {
                                posterBitmap?.let { bitmap ->
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "Movie Poster",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(300.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(
                                        text = "Title:",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        modifier = Modifier.width(100.dp) // Adjust width for alignment
                                    )
                                    Text(
                                        text = it.title,
                                        color = Color.Gray,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(
                                        text = "Year:",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        modifier = Modifier.width(100.dp)
                                    )
                                    Text(
                                        text = it.year,
                                        color = Color.Gray,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(
                                        text = "Rated:",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        modifier = Modifier.width(100.dp)
                                    )
                                    Text(
                                        text = it.rated,
                                        color = Color.Gray,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(
                                        text = "Released:",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        modifier = Modifier.width(100.dp)
                                    )
                                    Text(
                                        text = it.released,
                                        color = Color.Gray,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(
                                        text = "Runtime:",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        modifier = Modifier.width(100.dp)
                                    )
                                    Text(
                                        text = it.runtime,
                                        color = Color.Gray,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(
                                        text = "Genre:",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        modifier = Modifier.width(100.dp)
                                    )
                                    Text(
                                        text = it.genre,
                                        color = Color.Gray,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(
                                        text = "Director:",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        modifier = Modifier.width(100.dp)
                                    )
                                    Text(
                                        text = it.director,
                                        color = Color.Gray,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(
                                        text = "Writer:",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        modifier = Modifier.width(100.dp)
                                    )
                                    Text(
                                        text = it.writer,
                                        color = Color.Gray,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(
                                        text = "Actors:",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        modifier = Modifier.width(100.dp)
                                    )
                                    Text(
                                        text = it.actors,
                                        color = Color.Gray,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Plot Icon",
                                        tint = Color(0xFFFF9800),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = it.plot,
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Button(
                                    onClick = {
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
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF14BB00),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(15.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                ) {
                                    Text("SAVE MOVIE TO DATABASE", fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }
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