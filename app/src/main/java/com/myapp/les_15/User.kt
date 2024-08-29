package com.myapp.les_15

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null
)