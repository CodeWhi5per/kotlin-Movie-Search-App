package com.example.moviedatabaseapp.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

import android.util.Log
import com.example.moviedatabaseapp.data.MovieApiResponse

suspend fun getMovieFromApi(title: String): MovieApiResponse? = withContext(Dispatchers.IO) {
    val apiKey = "def7327"
    val url = URL("http://www.omdbapi.com/?t=${URLEncoder.encode(title, "UTF-8")}&apikey=$apiKey")
    val connection = url.openConnection() as HttpURLConnection

    return@withContext try {
        val response = connection.inputStream.bufferedReader().readText()
        Log.d("API_RESPONSE", response)
        val json = JSONObject(response)

        if (json.getString("Response") == "True") {
            MovieApiResponse(
                imdbID = json.getString("imdbID"),
                title = json.getString("Title"),
                year = json.getString("Year"),
                rated = json.getString("Rated"),
                released = json.getString("Released"),
                runtime = json.getString("Runtime"),
                genre = json.getString("Genre"),
                director = json.getString("Director"),
                writer = json.getString("Writer"),
                actors = json.getString("Actors"),
                plot = json.getString("Plot"),
                poster = json.getString("Poster")
            )
        } else {
            Log.e("API_ERROR", "Error: ${json.getString("Error")}")
            null
        }
    } catch (e: Exception) {
        Log.e("API_ERROR", "Exception: ${e.message}")
        null
    } finally {
        connection.disconnect()
    }
}

suspend fun searchMoviesByTitle(query: String): List<MovieApiResponse> = withContext(Dispatchers.IO) {
    val apiKey = "def7327"
    val url = URL("http://www.omdbapi.com/?s=${URLEncoder.encode(query, "UTF-8")}&apikey=$apiKey")
    val connection = url.openConnection() as HttpURLConnection

    return@withContext try {
        val response = connection.inputStream.bufferedReader().readText()
        Log.d("API_RESPONSE", response)
        val json = JSONObject(response)

        if (json.getString("Response") == "True") {
            val searchResults = json.getJSONArray("Search")
            List(searchResults.length()) { index ->
                val movie = searchResults.getJSONObject(index)
                MovieApiResponse(
                    imdbID = movie.getString("imdbID"),
                    title = movie.getString("Title"),
                    year = movie.getString("Year"),
                    rated = "",
                    released = "",
                    runtime = "",
                    genre = "",
                    director = "",
                    writer = "",
                    actors = "",
                    plot = "",
                    poster = movie.getString("Poster")
                )
            }
        } else {
            Log.e("API_ERROR", "Error: ${json.getString("Error")}")
            emptyList()
        }
    } catch (e: Exception) {
        Log.e("API_ERROR", "Exception: ${e.message}")
        emptyList()
    } finally {
        connection.disconnect()
    }
}