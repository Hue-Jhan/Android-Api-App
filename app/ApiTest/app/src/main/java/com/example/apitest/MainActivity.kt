package com.example.apitest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.apitest.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.*
// import retrofit2.Call
// import retrofit2.Callback
// import retrofit2.Response
import java.io.IOException
import android.os.Build
import androidx.appcompat.widget.Toolbar
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()
    private lateinit var dogImageView: ImageView
    private lateinit var fetchDogImageButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor("#4CAF50")

        dogImageView = binding.dogImageView
        val progressBar: ProgressBar = binding.progressBar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "                  Api App Test xd"

        binding.dogButton.setOnClickListener {
            val dogImageFetcher = DogImageFetcher(dogImageView, findViewById(R.id.progressBar))
                dogImageFetcher.fetchRandomDogImage() // Call fetch from DogImageFetcher
        }

        // Set up button click listener
        binding.fetchButton.setOnClickListener {
            fetchInsult()
        }

        binding.fetchJokeButton.setOnClickListener {
            fetchJoke()
        }
    }


    private fun changeStatusBarColor(color: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = android.graphics.Color.parseColor(color)
        }
    }

    /* private fun fetchRandomDogImage() {
        // Make an API call to get a random dog image
        RetrofitClient.instance.getRandomDogImage().enqueue(object : Callback<DogImageResponse> {
            override fun onResponse(call: Call<DogImageResponse>, response: Response<DogImageResponse>) {
                if (response.isSuccessful) {
                    // Extract the image URL from the response
                    val imageUrl = response.body()?.message
                    if (imageUrl != null) {
                        // Use Glide to load the image into the ImageView
                        Glide.with(this@MainActivity)
                            .load(imageUrl)
                            .into(dogImageView)
                    } else {
                        Toast.makeText(this@MainActivity, "No image found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DogImageResponse>, t: Throwable) {
                // Handle the error
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    } */


    // Function to fetch insult from the API
    private fun fetchInsult() {
        val url = "https://evilinsult.com/generate_insult.php?lang=en&type=json"
        // val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        // Making the network call asynchronously
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "Failed to fetch insult: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (responseBody != null) {
                    val gson = Gson()
                    val insultResponse = gson.fromJson(responseBody, InsultResponse::class.java)

                    // Update the UI on the main thread
                    runOnUiThread {
                        binding.insultTextView.text = insultResponse.insult
                    }
               /* } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to load insult",
                            Toast.LENGTH_SHORT
                        ).show()
                    }*/
                }
            }
        })
    }

    private fun fetchJoke() {
        val url = "https://official-joke-api.appspot.com/jokes/random"

        val request = Request.Builder()
            .url(url)
            .build()

        // Making the network call asynchronously
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "Failed to fetch joke: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (responseBody != null) {
                    val gson = Gson()
                    val jokeResponse = gson.fromJson(responseBody, JokeResponse::class.java)

                    // Update the UI on the main thread
                    runOnUiThread {
                        binding.setupTextView.text = jokeResponse.setup
                        binding.punchlineTextView.text = jokeResponse.punchline
                    }
                }
            }
        })
    }

    // Data class to model the API response
    data class InsultResponse(
        val number: String,
        val language: String,
        val insult: String,
        val created: String,
        val shown: String,
        val createdby: String,
        val active: String,
        val comment: String
    )
    data class JokeResponse(
        val type: String,
        val setup: String,
        val punchline: String,
        val id: Int
    )
}
