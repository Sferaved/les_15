package com.myapp.les_15.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Product>)

    @Query("SELECT * FROM Product")
    suspend fun getAllItems(): List<Product>
}