package com.example.kotlinware.ui.minigames

import androidx.compose.ui.geometry.Offset

interface MinigameInterface {
    fun resetMinigame()
    fun success() : Boolean
    fun fail(): Boolean
    fun update(deltaTime: Long)
    fun checkCollision(offset: Offset)
}