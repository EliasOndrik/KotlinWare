package com.example.kotlinware.ui.minigames.swiping

import androidx.compose.ui.geometry.Offset
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoveMeNotViewModel: MinigameInterface {
    private val _leafs = MutableStateFlow(listOf(
        Leaf(50f,-50f,0f,false),
        Leaf(50f,-50f,45f,false),
        Leaf(50f,-50f,90f,false),
        Leaf(50f,-50f,135f,false),
        Leaf(50f,-50f,180f,false),
        Leaf(50f,-50f,225f,false),
        Leaf(50f,-50f,270f,false),
        Leaf(50f,-50f,315f,false),
    ))
    val leafs = _leafs.asStateFlow()
    private var currentLeaf = -1
    private val leafLength = 300f
    private val gravity = 2f

    override fun resetMinigame() {
        _leafs.update {
            it.map {leaf->
                leaf.copy(x = 50f, y = -50f, collected = false)
            }
        }
        currentLeaf = -1
    }

    override fun success(): Boolean {
        var success = true
        leafs.value.forEach {
            success = success && it.collected
        }
        return success

    }

    override fun fail(): Boolean {
        return false
    }

    override fun update(deltaTime: Long) {
        _leafs.update {
            it.map { leaf ->
                if (!leaf.collected){
                    leaf
                }else{
                    leaf.copy(y = leaf.y + gravity)
                }
            }
        }

    }

    override fun checkCollision(offset: Offset) {
        var angle = Math.toDegrees(kotlin.math.atan2(offset.y,offset.x).toDouble())
        if (angle<0){
            angle+=360f
        }
        val distance = offset.getDistance()
        for (i in 0..<leafs.value.size){
            if (leafs.value[i].collected){
                continue
            }
            if (distance<=leafLength && angle<=leafs.value[i].angle+15f && angle>=leafs.value[i].angle-15f){
                currentLeaf = i
            }
        }
    }
    fun update(dragAmount: Offset){
        if (currentLeaf<0){
            return
        }
        var angle = Math.toDegrees( kotlin.math.atan2(dragAmount.y,dragAmount.x).toDouble())
        if (angle<0){
            angle+=360f
        }
        if (angle<=leafs.value[currentLeaf].angle+45f && angle>=leafs.value[currentLeaf].angle-45f){
            _leafs.update {
                it.toMutableList().apply {
                    this[currentLeaf] = this[currentLeaf].copy(collected = true, x = this[currentLeaf].x + dragAmount.x, y = this[currentLeaf].y + dragAmount.y)
                }
            }
            currentLeaf = -1
        }
    }
}

data class Leaf(
    var x: Float,
    var y: Float,
    var angle: Float,
    var collected: Boolean,
)

