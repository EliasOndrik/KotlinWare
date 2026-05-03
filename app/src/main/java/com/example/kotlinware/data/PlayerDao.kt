package com.example.kotlinware.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao : GenericDao<Player>{
    @Query("SELECT * from players LIMIT 1")
    fun getFirst(): Flow<Player>

    @Query("SELECT * from players")
    fun getAllPlayers(): Flow<List<Player>>
}