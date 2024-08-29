package com.myapp.les_15.database


import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("products")
    val products: List<Product?>? = null
)
