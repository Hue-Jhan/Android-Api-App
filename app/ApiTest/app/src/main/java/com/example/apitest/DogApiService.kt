package com.example.apitest

import retrofit2.Call
import retrofit2.http.GET

// Define the API service
interface DogApiService {
    @GET("breeds/image/random")
    fun getRandomDogImage(): Call<DogImageResponse>
}
