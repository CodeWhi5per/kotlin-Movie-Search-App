package com.example.moviedatabaseapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieApiResponse(
    val imdbID: String,
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
    val poster: String,
    val imdbRating: String
) : Parcelable