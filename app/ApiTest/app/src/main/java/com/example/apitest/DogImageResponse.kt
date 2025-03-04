package com.example.apitest

// Data class to hold the response
data class DogImageResponse(
    val message: String,  // URL of the dog image
    val status: String    // "success" or "failure"
)
