package com.example.moviedatabaseapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates the class as a Room database with the Movie entity.
@Database(entities = [Movie::class], version = 3)
abstract class MovieDatabase : RoomDatabase() {

    // Abstract method to get the DAO for accessing the Movie table
    abstract fun movieDao(): MovieDao

    companion object {
        // Volatile instance to ensure visibility of changes across threads
        @Volatile private var INSTANCE: MovieDatabase? = null

        // Returns the singleton instance of the database
        fun getDatabase(context: Context): MovieDatabase {
            // Check if the instance is null, and if so, create a new database instance
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_db" // Name of the database file
                )
                    .fallbackToDestructiveMigration() // Clears and rebuilds the database if the schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}