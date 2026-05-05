package com.example.kotlinware.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: Player): Long
    @Update
    suspend fun update(obj: Player): Int?
    @Delete
    suspend fun delete(obj: Player): Int?
    @Query("SELECT * from players LIMIT 1")
    fun getFirst(): Flow<Player>

    @Query("SELECT * from players")
    fun getAllPlayers(): Flow<List<Player>>
}