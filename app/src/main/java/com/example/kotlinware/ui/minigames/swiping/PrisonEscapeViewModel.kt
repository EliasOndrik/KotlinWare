package com.example.kotlinware.ui.minigames.swiping

import androidx.compose.ui.geometry.Offset
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class PrisonEscapeViewModel: MinigameInterface {
    private val _breakPoints = MutableStateFlow(listOf(
        BreakPoint(80f,250f,40f,50f,true),
        BreakPoint(-120f,250f,40f,50f,true),
        BreakPoint(80f,-300f,40f,50f,true),
        BreakPoint(-120f,-300f,40f,50f,true),
        BreakPoint(250f,80f,50f,40f,true),
        BreakPoint(250f,-120f,50f,40f,true),
        BreakPoint(-300f,80f,50f,40f,true),
        BreakPoint(-300f,-120f,50f,40f,true),
    ))
    val breakPoints = _breakPoints.asStateFlow()
    private var playerOffset: Offset = Offset(0f,0f)
    override fun resetMinigame() {
        for (i in 0..5){
            val rand = Random.nextInt(_breakPoints.value.size)
            _breakPoints.update {
                it.toMutableList().apply {
                    this[rand] = this[rand].copy(broken = false)
                }
            }
        }
    }

    override fun success(): Boolean {
        var success = true
        _breakPoints.value.forEach {
            success = success && it.broken
        }
        return success
    }

    override fun fail(): Boolean {
        return false
    }

    override fun update(deltaTime: Long) {

    }

    override fun checkCollision(offset: Offset) {
        playerOffset = offset
    }
    fun update(dragAmount: Offset){
        val strength = dragAmount.getDistance()
        val currentOffset = playerOffset
        playerOffset+=dragAmount
        val middlePoint = (playerOffset + currentOffset)/2f
        if (strength>=30f){
            _breakPoints.update {
                it.toMutableList().apply {
                    for (i in 0..<this.size){
                        if (this[i].broken){
                            continue
                        }
                        if (middlePoint.x >=this[i].x && middlePoint.x <= this[i].x + this[i].width &&
                            middlePoint.y >=this[i].y && middlePoint.y <= this[i].y + this[i].height){
                            this[i] = this[i].copy(broken = true)
                        }
                    }
                }
            }
        }

    }
}

data class BreakPoint(
    var x: Float,
    var y: Float,
    var width: Float,
    var height:Float,
    var broken: Boolean
)