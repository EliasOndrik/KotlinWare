package com.example.kotlinware.ui.minigames.draging

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class LightCandleViewModel: MinigameInterface {

    private val _candles = MutableStateFlow(listOf(
        Candle(0f,-300f, false, 100L),
        Candle(0f,-200f, false, 100L),
        Candle(0f,-100f, false, 100L),
    ))
    val candles = _candles.asStateFlow()
    private val _lightOffset = MutableStateFlow(Offset(300f,-300f))
    val lightOffset = _lightOffset.asStateFlow()
    private val lightSize = Size(300f, 100f)
    private var carrying = false
    private var previousTimer = 0L

    override fun resetMinigame() {
        _lightOffset.update { Offset(300f,-300f) }
        _candles.update {
            it.map { candle ->
                candle.copy(x = Random.nextInt(-300,300).toFloat(), lit = false, lightTimer = 200L)
            }
        }
        carrying = false
        previousTimer = 0L
    }

    override fun success(): Boolean {
        var success = true
        for (i in 0..<_candles.value.size){
            success = _candles.value[i].lit && success
        }
        return success
    }

    override fun fail(): Boolean {
        return false
    }

    override fun update(deltaTime: Long) {
        if (previousTimer == 0L){
            previousTimer = deltaTime
        }
        val delta = previousTimer - deltaTime

        _candles.update {
            it.toMutableList().apply {
                for(i in 0 ..<this.size){
                    if (this[i].lit){
                        continue
                    }
                    if (this[i].lightTimer<=0){
                        this[i] = this[i].copy(lit = true)
                    }
                    val distance = (Offset(this[i].x,this[i].y) - Offset(_lightOffset.value.x, _lightOffset.value.y+lightSize.height)).getDistance()
                    if (distance <= 50){
                        this[i] = this[i].copy(lightTimer = this[i].lightTimer-delta)
                    }

                }
            }
        }

        previousTimer = deltaTime

    }

    override fun checkCollision(offset: Offset) {
        if(offset.x >= _lightOffset.value.x && offset.x <= _lightOffset.value.x + lightSize.width &&
            offset.y >= _lightOffset.value.y && offset.y <= _lightOffset.value.y + lightSize.height){
            carrying = true
        }
    }
    fun updatePosition(dragAmount: Offset){
        _lightOffset.update { it.copy(x = it.x + dragAmount.x, y = it.y + dragAmount.y) }
    }

    fun dropIt(){
        carrying = false
    }
}

data class Candle(
    var x: Float,
    var y: Float,
    var lit: Boolean,
    var lightTimer: Long
)