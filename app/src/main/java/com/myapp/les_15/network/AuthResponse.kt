package com.myapp.les_15.network

data class AuthResponse(
    val email: String,
    val firstname: String,
    val gender: String,
    val id: Int,
    val image: String,
    val lastname: String,
    val token: String,
    val username: String
)