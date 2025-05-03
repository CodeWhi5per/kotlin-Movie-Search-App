package com.example.moviedatabaseapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("addMovies") }) {
            Text("Add Movies to DB")
        }
        Button(onClick = { navController.navigate("searchMovie") }) {
            Text("Search for Movies")
        }
        Button(onClick = { /* navController.navigate("searchActors") */ }) {
            Text("Search for Actors")
        }
    }
}
