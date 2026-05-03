package com.example.kotlinware.data

import kotlinx.coroutines.flow.Flow

interface GameInterfaceRepository {
    suspend fun insertMinigame(minigame: Minigame)
    suspend fun updateMinigame(minigame: Minigame)
    suspend fun deleteMinigame(minigame: Minigame)
    fun getAllMinigames() : Flow<List<Minigame>>
    fun getAllPlayableMinigames() : Flow<List<Minigame>>

    suspend fun insertPlayer(player: Player)
    suspend fun updatePlayer(player: Player)
    suspend fun deletePlayer(player: Player)
    fun getPlayer(): Flow<Player>
}