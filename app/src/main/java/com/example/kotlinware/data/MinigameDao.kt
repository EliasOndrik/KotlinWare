package com.example.kotlinware.data

import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface MinigameDao : GenericDao<Minigame>{
    @Query("SELECT * from minigames WHERE id = :id")
    fun getMinigame(id: Int): Flow<Minigame>

    @Query("SELECT * from minigames ")
    fun getAllMinigames(): Flow<List<Minigame>>

    @Query("SELECT * from minigames where unlocked == 1")
    fun getAllPlayableMinigames(): Flow<List<Minigame>>
}