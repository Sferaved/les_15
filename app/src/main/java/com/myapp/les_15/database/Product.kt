package com.myapp.les_15.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.myapp.les_15.Converters

@Entity
data class Product(
    @PrimaryKey
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("price")
    val price: Double? = null,

    @SerializedName("discountPercentage")
    val discountPercentage: Double? = null,

    @SerializedName("rating")
    val rating: Double? = null,

    @SerializedName("stock")
    val stock: Int? = null,

    @SerializedName("brand")
    val brand: String? = null,

    @SerializedName("sku")
    val sku: String? = null,

    @SerializedName("weight")
    val weight: Int? = null,

    @TypeConverters(DimensionsConverter::class)
    @SerializedName("dimensions")
    val dimensions: Dimensions? = null,

    @SerializedName("warrantyInformation")
    val warrantyInformation: String? = null,

    @SerializedName("shippingInformation")
    val shippingInformation: String? = null,

    @SerializedName("availabilityStatus")
    val availabilityStatus: String? = null,

    @TypeConverters(ReviewConverter::class)
    @SerializedName("reviews")
    val reviews: List<Review>? = null,

    @SerializedName("returnPolicy")
    val returnPolicy: String? = null,

    @SerializedName("minimumOrderQuantity")
    val minimumOrderQuantity: Int? = null,

    @TypeConverters(MetaConverter::class)
    @SerializedName("meta")
    val meta: Meta? = null,

    @TypeConverters(StringListConverter::class)
    @SerializedName("tags")
    val tags: List<String>? = null,

    @TypeConverters(Converters::class)
    @SerializedName("images")
    val images: List<String?>? = null,

    @SerializedName("thumbnail")
    val thumbnail: String? = null
)

// Nested Data Classes
data class Dimensions(
    @SerializedName("width")
    val width: Double? = null,

    @SerializedName("height")
    val height: Double? = null,

    @SerializedName("depth")
    val depth: Double? = null
)


data class Review(
    @SerializedName("rating")
    val rating: Int? = null,
    @SerializedName("comment")
    val comment: String? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("reviewerName")
    val reviewerName: String? = null,
    @SerializedName("reviewerEmail")
    val reviewerEmail: String? = null
)

data class Meta(
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    @SerializedName("barcode")
    val barcode: String? = null,
    @SerializedName("qrCode")
    val qrCode: String? = null
)

// Type Converters for Room
class DimensionsConverter {

    @TypeConverter
    fun fromDimensions(dimensions: Dimensions?): String? {
        // Convert Dimensions object to a JSON string
        return dimensions?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toDimensions(dimensionsString: String?): Dimensions? {
        // Convert JSON string back to a Dimensions object
        return dimensionsString?.let {
            val type = object : TypeToken<Dimensions>() {}.type
            Gson().fromJson(it, type)
        }
    }
}


class ReviewConverter {
    @TypeConverter
    fun fromReviewList(reviews: List<Review>?): String? {
        return Gson().toJson(reviews)
    }

    @TypeConverter
    fun toReviewList(data: String?): List<Review>? {
        val listType = object : TypeToken<List<Review>>() {}.type
        return Gson().fromJson(data, listType)
    }
}

class MetaConverter {
    @TypeConverter
    fun fromMeta(meta: Meta?): String? {
        return Gson().toJson(meta)
    }

    @TypeConverter
    fun toMeta(data: String?): Meta? {
        return Gson().fromJson(data, Meta::class.java)
    }
}

class StringListConverter {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(data: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(data, listType)
    }
}
