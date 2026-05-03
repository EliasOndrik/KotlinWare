package com.example.kotlinware.data

import kotlinx.coroutines.flow.Flow

class GameRepository(
    private val playerDao: PlayerDao,
    private val minigameDao: MinigameDao
) : GameInterfaceRepository {
    override suspend fun insertMinigame(minigame: Minigame) = minigameDao.insert(minigame)

    override suspend fun updateMinigame(minigame: Minigame) = minigameDao.update(minigame)

    override suspend fun deleteMinigame(minigame: Minigame) = minigameDao.delete(minigame)

    override fun getAllMinigames(): Flow<List<Minigame>> = minigameDao.getAllMinigames()

    override fun getAllPlayableMinigames(): Flow<List<Minigame>> = minigameDao.getAllPlayableMinigames()

    override suspend fun insertPlayer(player: Player) = playerDao.insert(player)

    override suspend fun updatePlayer(player: Player) = playerDao.update(player)

    override suspend fun deletePlayer(player: Player) = playerDao.delete(player)

    override fun getPlayer(): Flow<Player> = playerDao.getFirst()

}