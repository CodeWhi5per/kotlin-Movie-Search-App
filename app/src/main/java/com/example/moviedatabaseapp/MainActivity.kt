package com.example.moviedatabaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.moviedatabaseapp.data.MovieDatabase
import com.example.moviedatabaseapp.screens.AddMoviesScreen
import com.example.moviedatabaseapp.screens.MainScreen
import com.example.moviedatabaseapp.screens.SearchMovieScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = MovieDatabase.getDatabase(applicationContext)
        val movieDao = db.movieDao()

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    MainScreen(navController = navController)
                }
                composable("addMovies") {
                    AddMoviesScreen(movieDao = movieDao, navController = navController)
                }
                composable("searchMovie") {
                    SearchMovieScreen(movieDao = movieDao)
                }
            }
        }
    }
}
