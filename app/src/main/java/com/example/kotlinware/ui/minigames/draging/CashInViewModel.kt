package com.example.kotlinware.ui.minigames.draging

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class CashInViewModel: MinigameInterface {
    private val _coins = MutableStateFlow(listOf(
        Coin(0f,0f,false),
        Coin(0f,0f,false),
        Coin(0f,0f,false),
        Coin(0f,0f,false),
    ))
    val coins = _coins.asStateFlow()
    private val coinRadius = 50f
    private var draggingCoin = -1
    val purseSize = Size(400f, 200f)
    val purseOffset = Offset(-200f,200f)

    override fun resetMinigame() {
        _coins.update { currentList ->
            currentList.map {coin ->
                coin.copy(x = Random.nextInt(-400, 400).toFloat(), y = Random.nextInt(-400, 0).toFloat(),collected = false)
            }
        }
        draggingCoin = -1
    }

    override fun success(): Boolean {
        var success = true
        coins.value.forEach {
            success = success && it.collected
        }
        return success
    }

    override fun fail(): Boolean {
        return false
    }

    override fun update(deltaTime: Long) {
    }

    override fun checkCollision(offset: Offset) {
        for (i in 0..<coins.value.size){
            if (coins.value[i].collected){
                continue
            }
            val distance = (offset - Offset(coins.value[i].x,coins.value[i].y)).getDistance()
            if (distance <= coinRadius){
                draggingCoin = i
                break
            }
        }
    }
    fun dropCoins(){
        draggingCoin = -1
    }
    fun updatePosition(dragAmount: Offset){
        if (draggingCoin < 0){
            return
        }
        _coins.update { currentList->
            currentList.toMutableList().apply {
                this[draggingCoin] = this[draggingCoin].copy(x = this[draggingCoin].x + dragAmount.x, y = this[draggingCoin].y + dragAmount.y)
                if (this[draggingCoin].x >= purseOffset.x && this[draggingCoin].x <= purseOffset.x + purseSize.width &&
                    this[draggingCoin].y >= purseOffset.y && this[draggingCoin].y <= purseOffset.y + purseSize.height){
                    this[draggingCoin] = this[draggingCoin].copy(collected = true)
                }
            }
        }
    }
}

data class Coin(
    var x: Float,
    var y: Float,
    var collected: Boolean
)