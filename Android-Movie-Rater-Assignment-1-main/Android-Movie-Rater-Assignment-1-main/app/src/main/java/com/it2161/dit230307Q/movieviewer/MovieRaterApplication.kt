package com.it2161.dit230307Q.movieviewer

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.it2161.dit230307Q.movieviewer.data.*
import jsonData
import org.json.JSONArray
import org.json.JSONException
import java.io.File

class MovieRaterApplication : Application() {
    companion object {
        lateinit var instance: MovieRaterApplication
            private set
    }

    var data = mutableListOf<MovieItem>()
        set(value) {
            field = value
            saveListFile(applicationContext)
        }

    var userProfile: UserProfile? = null
        set(value) {
            if (value != null) {
                field = value
                saveProfileToFile(applicationContext)
            }
        }

    override fun onCreate() {
        super.onCreate()
        instance = this
        loadData(applicationContext)
    }

    private fun loadMovieDataIntoList(context: Context) {
        try {
            val jsonArray = JSONArray(jsonData)
            val file = File(context.filesDir, "movielist.dat")
            if (!file.exists()) {
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)

                    if (!jsonObject.has("id")) {
                        Log.e("MovieRaterApplication", "Missing 'id' field in JSON object at index $i")
                        continue
                    }

                    val id = jsonObject.getInt("id")
                    val title = jsonObject.optString("title", "Unknown Title")
                    val director = jsonObject.optString("director", "Unknown Director")
                    val releaseDate = jsonObject.optString("release_date", "Unknown Date")
                    val rating = jsonObject.optString("rating", "0.0/10").split("/")[0].toFloatOrNull() ?: 0.0f
                    val poster = jsonObject.optString("image", "")
                    val genre = jsonObject.optString("genre", "Unknown Genre")
                    val length = jsonObject.optInt("length", 0)
                    val synopsis = jsonObject.optString("synopsis", "No synopsis available")
                    val overview = jsonObject.optString("overview", "No overview available")
                    val posterPath = jsonObject.optString("poster_path", "")
                    val voteAverage = jsonObject.optDouble("vote_average", 0.0).toFloat()

                    val actorsArray = jsonObject.optJSONArray("actors")
                    val actors = mutableListOf<String>()
                    actorsArray?.let {
                        for (j in 0 until it.length()) {
                            actors.add(it.optString(j, "Unknown Actor"))
                        }
                    }

                    val commentsArray = jsonObject.optJSONArray("comments")
                    val listOfComments = mutableListOf<Comments>()
                    commentsArray?.let {
                        for (j in 0 until it.length()) {
                            val commentsObj = it.getJSONObject(j)
                            val user = commentsObj.optString("user", "Anonymous")
                            val comment = commentsObj.optString("comment", "No comment")
                            val date = commentsObj.optString("date", "Unknown Date")
                            val time = commentsObj.optString("time", "Unknown Time")
                            listOfComments.add(Comments(user, comment, date, time))
                        }
                    }

                    data.add(
                        MovieItem(
                            id = id,
                            title = title,
                            overview = overview,
                            poster_path = posterPath,
                            vote_average = voteAverage,
                            director = director,
                            releaseDate = releaseDate,
                            ratings_score = rating,
                            actors = actors,
                            image = poster,
                            genre = genre,
                            length = length,
                            synopsis = synopsis,
                            comment = listOfComments
                        )
                    )
                }
            } else {
                val jsonString = file.readText()
                val gson = Gson()
                val type = object : TypeToken<List<MovieItem>>() {}.type
                data = gson.fromJson(jsonString, type) ?: mutableListOf()
            }
        } catch (e: JSONException) {
            Log.e("MovieRaterApplication", "JSON Parsing error: ${e.message}")
        } catch (e: Exception) {
            Log.e("MovieRaterApplication", "Unexpected error: ${e.message}")
        }
    }

    private fun saveListFile(context: Context) {
        val gson = Gson()
        val jsonString = gson.toJson(data)
        val file = File(context.filesDir, "movielist.dat")
        file.writeText(jsonString)
    }

    private fun saveProfileToFile(context: Context) {
        val gson = Gson()
        val jsonString = gson.toJson(userProfile)
        val file = File(context.filesDir, "profile.dat")
        file.writeText(jsonString)
    }

    private fun loadProfileFromFile(context: Context) {
        val file = File(context.filesDir, "profile.dat")
        userProfile = try {
            val jsonString = file.readText()
            val gson = Gson()
            gson.fromJson(jsonString, object : TypeToken<UserProfile>() {}.type)
        } catch (e: Exception) {
            null
        }
    }

    private fun loadData(context: Context) {
        loadProfileFromFile(context)
        loadMovieDataIntoList(context)
    }

    fun getImgVector(fileName: String): Bitmap {
        val dataValue = when (fileName) {
            "IntoTheUnknown" -> mvIntoTheUnknownData
            "EchosOfEternity" -> mvEchosOfEternityData
            "LostInTime" -> mvLostInTimeData
            "ShadowsOfThePast" -> mvShadowsOfthePastData
            "BeneathTheSurface" -> mvBeneathTheSurface
            "LastFrontier" -> mvTheLastFrontierData
            "CityOfShadows" -> mvCityOfShadowsData
            "SilentStorm" -> mvTheSilentStormData
            else -> ""
        }

        return try {
            val imageBytes = Base64.decode(dataValue, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } catch (e: Exception) {
            Log.e("MovieRaterApplication", "Error decoding image: ${e.message}")
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Return a blank bitmap on error
        }
    }
}
