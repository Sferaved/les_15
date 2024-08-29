package com.myapp.les_15.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myapp.les_15.Converters

@Database(entities = [Product::class], version = 2, exportSchema = false)
@TypeConverters(DimensionsConverter::class, ReviewConverter::class, MetaConverter::class, StringListConverter::class, Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
}