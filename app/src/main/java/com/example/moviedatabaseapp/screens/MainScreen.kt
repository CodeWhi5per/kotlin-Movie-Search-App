package com.example.moviedatabaseapp.screens

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moviedatabaseapp.R
import com.example.moviedatabaseapp.data.Movie
import com.example.moviedatabaseapp.data.MovieDao
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope

// Hardcoded list of movies to be added to the database
val hardcodedMovies = listOf(
    Movie(
        imdbID = "tt0111161",
        title = "The Shawshank Redemption",
        year = "1994",
        rated = "R",
        released = "14 Oct 1994",
        runtime = "142 min",
        genre = "Drama",
        director = "Frank Darabont",
        writer = "Stephen King, Frank Darabont",
        actors = "Tim Robbins, Morgan Freeman, Bob Gunton",
        plot = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
        poster = "https://m.media-amazon.com/images/M/MV5BMDAyY2FhYjctNDc5OS00MDNlLThiMGUtY2UxYWVkNGY2ZjljXkEyXkFqcGc@._V1_SX300.jpg"
    ),
    Movie(
        imdbID = "tt2313197",
        title = "Batman: The Dark Knight Returns, Part 1",
        year = "2012",
        rated = "PG-13",
        released = "25 Sep 2012",
        runtime = "76 min",
        genre = "Animation, Action, Crime, Drama, Thriller",
        director = "Jay Oliva",
        writer = "Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob Goodman",
        actors = "Peter Weller, Ariel Winter, David Selby, Wade Williams",
        plot = "Batman has not been seen for ten years. A new breed of criminal ravages Gotham City, forcing 55-year-old Bruce Wayne back into the cape and cowl. But, does he still have what it takes to fight crime in a new era?",
        poster = "https://m.media-amazon.com/images/M/MV5BMzIxMDkxNDM2M15BMl5BanBnXkFtZTcwMDA5ODY1OQ@@._V1_SX300.jpg"
    ),
    Movie(
        imdbID = "tt0167260",
        title = "The Lord of the Rings: The Return of the King",
        year = "2003",
        rated = "PG-13",
        released = "17 Dec 2003",
        runtime = "201 min",
        genre = "Action, Adventure, Drama",
        director = "Peter Jackson",
        writer = "J.R.R. Tolkien, Fran Walsh, Philippa Boyens",
        actors = "Elijah Wood, Viggo Mortensen, Ian McKellen",
        plot = "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.",
        poster = "https://m.media-amazon.com/images/M/MV5BMTZkMjBjNWMtZGI5OC00MGU0LTk4ZTItODg2NWM3NTVmNWQ4XkEyXkFqcGc@._V1_SX300.jpg"
    ),
    Movie(
        imdbID = "tt1375666",
        title = "Inception",
        year = "2010",
        rated = "PG-13",
        released = "16 Jul 2010",
        runtime = "148 min",
        genre = "Action, Adventure, Sci-Fi",
        director = "Christopher Nolan",
        writer = "Christopher Nolan",
        actors = "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
        plot = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.",
        poster = "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_SX300.jpg"
    ),
    Movie(
        imdbID = "tt0133093",
        title = "The Matrix",
        year = "1999",
        rated = "R",
        released = "31 Mar 1999",
        runtime = "136 min",
        genre = "Action, Sci-Fi",
        director = "Lana Wachowski, Lilly Wachowski",
        writer = "Lilly Wachowski, Lana Wachowski",
        actors = "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
        plot = "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence.",
        poster = "https://m.media-amazon.com/images/M/MV5BN2NmN2VhMTQtMDNiOS00NDlhLTliMjgtODE2ZTY0ODQyNDRhXkEyXkFqcGc@._V1_SX300.jpg"
    )
)

@Composable
fun MainScreen(navController: NavController, movieDao: MovieDao) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Main container for the screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (isLandscape) {
            // Layout for landscape orientation
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Background image
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "Background Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(350.dp)
                        .height(260.dp)
                )
                // Search and action buttons
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SearchSection(navController, searchQuery) { searchQuery = it }
                    Spacer(modifier = Modifier.height(20.dp))
                    ActionButtons(navController, movieDao, scope, context)
                }
            }
        } else {
            // Layout for portrait orientation
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(120.dp))

                // Background image
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "Background Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(700.dp)
                        .height(260.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Search and action buttons
                SearchSection(navController, searchQuery) { searchQuery = it }
                Spacer(modifier = Modifier.height(50.dp))
                ActionButtons(navController, movieDao, scope, context)
            }
        }
    }
}

@Composable
fun SearchSection(
    navController: NavController,
    searchQuery: TextFieldValue,
    onSearchQueryChange: (TextFieldValue) -> Unit
) {
    // Search bar and button
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search input field
        OutlinedTextField(
            value = searchQuery,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            onValueChange = onSearchQueryChange,
            placeholder = { Text("SEARCH") },
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF212121),
                unfocusedContainerColor = Color(0xFF212121),
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Search button
        Button(
            onClick = {
                navController.navigate("searchByTitle?query=${searchQuery.text}")
            },
            modifier = Modifier
                .height(56.dp)
                .width(56.dp),
            shape = RoundedCornerShape(20),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF009DFF),
                contentColor = Color.White
            )
        ) {
            Icon(
                Icons.Filled.Search,
                contentDescription = "Search Icon",
                modifier = Modifier.size(35.dp)
            )
        }
    }
}

@Composable
fun ActionButtons(
    navController: NavController,
    movieDao: MovieDao,
    scope: CoroutineScope,
    context: Context
) {
    // Buttons for various actions
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        // Button to add hardcoded movies to the database
        Button(
            onClick = {
                scope.launch {
                    movieDao.insertAll(hardcodedMovies)
                    Toast.makeText(context, "Movies added to DB", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF009DFF),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            Text("ADD MOVIES TO DB", fontSize = 15.sp)
        }
        // Button to navigate to the movie search screen
        Button(
            onClick = { navController.navigate("searchMovie") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF009DFF),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            Text("SEARCH FOR MOVIES", fontSize = 15.sp)
        }
        // Button to navigate to the actor search screen
        Button(
            onClick = { navController.navigate("searchActors") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF009DFF),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        ) {
            Text(text = "SEARCH FOR ACTORS", fontSize = 15.sp)
        }
    }
}