package com.example.moviedatabaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.moviedatabaseapp.data.MovieDatabase
import com.example.moviedatabaseapp.screens.MainScreen
import com.example.moviedatabaseapp.screens.SearchActorsScreen
import com.example.moviedatabaseapp.screens.SearchByTitleScreen
import com.example.moviedatabaseapp.screens.SearchMovieScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the database and retrieve the DAO for database operations
        val db = MovieDatabase.getDatabase(applicationContext)
        val movieDao = db.movieDao()

        setContent {
            // Set up navigation for the app with multiple screens
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    // Main screen displaying options or movie data
                    MainScreen(navController = navController, movieDao = movieDao)
                }
                composable("searchMovie") {
                    // Screen for searching movies
                    SearchMovieScreen(movieDao = movieDao)
                }
                composable("searchActors") {
                    // Screen for searching actors
                    SearchActorsScreen(movieDao = movieDao, navController = navController)
                }
                composable("searchByTitle?query={query}") { backStackEntry ->
                    // Screen for searching movies by title with a query parameter
                    val query = backStackEntry.arguments?.getString("query") ?: ""
                    SearchByTitleScreen(navController = navController, initialQuery = query)
                }
            }
        }
    }
}