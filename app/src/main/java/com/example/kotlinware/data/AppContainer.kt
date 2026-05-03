package com.example.kotlinware.data

import android.content.Context

interface AppContainer{
    val gameRepository : GameInterfaceRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val gameRepository: GameInterfaceRepository by lazy {
        val database = KotlinWareDatabase.getDatabase(context)
        GameRepository(database.playerDao(), database.minigameDao())
    }
}