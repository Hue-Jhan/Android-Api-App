package com.example.apitest

import retrofit2.Call
import retrofit2.http.GET


interface DogApiService {
    @GET("breeds/image/random")
    fun getRandomDogImage(): Call<DogImageResponse>
}
