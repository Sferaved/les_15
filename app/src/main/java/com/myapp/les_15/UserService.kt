package com.myapp.les_15

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UserService {
    @Headers("Accept: application/json")
    @GET("/users")
    suspend fun getUsers(): Result<List<User>>

    @Headers("Accept: application/json")
    @GET("/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Result<User>
}