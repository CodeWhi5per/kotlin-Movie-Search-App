package com.example.moviedatabaseapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Data Access Object (DAO) for interacting with the Movie database table

@Dao
interface MovieDao {

    // Inserts a list of movies into the database. If a conflict occurs, the existing record is replaced.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    // Retrieves all movies from the database as a Flow, allowing for reactive data updates.
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<Movie>>

    // retrieves a specific movies by actor name.
    @Query("SELECT * FROM movies WHERE actors LIKE '%' || :actor || '%' COLLATE NOCASE")
    fun findMoviesByActor(actor: String): Flow<List<Movie>>
}