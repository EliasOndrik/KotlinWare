package com.example.kotlinware.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface MinigameDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: Minigame): Long
    @Update
    suspend fun update(obj: Minigame): Int?
    @Delete
    suspend fun delete(obj: Minigame): Int?

    @Query("SELECT * from minigames WHERE name = :name")
    fun getMinigameByName(name:String): Flow<Minigame>
    @Query("SELECT * from minigames WHERE id = :id")
    fun getMinigame(id: Int): Flow<Minigame>

    @Query("SELECT * from minigames ")
    fun getAllMinigames(): Flow<List<Minigame>>

    @Query("SELECT * from minigames where unlocked == 1")
    fun getAllPlayableMinigames(): Flow<List<Minigame>>
}