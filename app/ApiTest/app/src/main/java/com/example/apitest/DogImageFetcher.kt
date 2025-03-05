package com.example.apitest

import android.widget.ImageView
import android.widget.Toast
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DogImageFetcher(private val imageView: ImageView, private val progressBar: ProgressBar) {

    fun fetchRandomDogImage() {
        progressBar.visibility = ProgressBar.VISIBLE
        
        RetrofitClient.instance.getRandomDogImage().enqueue(object : Callback<DogImageResponse> {
            override fun onResponse(call: Call<DogImageResponse>, response: Response<DogImageResponse>) {
                progressBar.visibility = ProgressBar.GONE
                
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.message
                    
                    if (imageUrl != null) {
                        // glide loads the image into the ImageView
                        Glide.with(imageView.context)
                            .load(imageUrl)
                            .into(imageView)
                    } else {
                        Toast.makeText(imageView.context, "No image found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(imageView.context, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DogImageResponse>, t: Throwable) {
                progressBar.visibility = ProgressBar.GONE;
                Toast.makeText(imageView.context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
