package com.example.moviedatabaseapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Define a Room database entity representing a movie

@Entity(tableName = "movies") // Specifies the table name in the database
data class Movie(
    @PrimaryKey val imdbID: String,  // Primary key
    val title: String,
    val year: String,
    val rated: String,
    val released: String,
    val runtime: String,
    val genre: String,
    val director: String,
    val writer: String,
    val actors: String,
    val plot: String,
    val poster: String
)
