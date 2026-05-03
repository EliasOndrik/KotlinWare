package com.example.kotlinware.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface GenericDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: T)
    @Update
    suspend fun update(obj: T)
    @Delete
    suspend fun delete(obj: T)
}