package com.myapp.les_15.network

import com.myapp.les_15.database.ProductResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @Headers("Accept: application/json", "No-Authentication: true")
    @POST("auth/login")
    suspend fun authenticate(@Body authRequest: AuthRequest): Result<AuthResponse>

    @Headers("Accept: application/json")
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ) :Result<ProductResponse>

}