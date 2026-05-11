package com.example.kotlinware.ui.minigames.tapping

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class CorrectOrderViewModel : MinigameInterface{
    private val _blocks = MutableStateFlow(listOf(
        Blocks(0f,0f, Color.Red, true),
        Blocks(0f,0f, Color.Green, true),
        Blocks(0f,0f, Color.Blue, true),
    ))
    val blocks = _blocks.asStateFlow()
    val size:Float = 100f

    override fun resetMinigame() {
        fail = false
        _blocks.update { currentList->
            currentList.map { block->
                block.copy(
                    x = Random.nextInt(-400,400).toFloat()-size/2,
                    y = Random.nextInt(-400,400).toFloat()-size/2,
                    visible = true
                )
            }
        }
    }
    private var fail = false

    override fun success() : Boolean {
        if (fail){
            return false
        }
        var success = true
        for (i in 0..<_blocks.value.size){
            success = success && !_blocks.value[i].visible
        }
        return success
    }

    override fun fail(): Boolean {
        return fail
    }

    override fun update(deltaTime: Long) {
    }

    override fun checkCollision(offset: Offset) {
        _blocks.update { currentList->
            var wasClicked = false
            currentList.map { block->
                if (wasClicked){
                    block
                } else if (offset.x > block.x && offset.x < block.x + size && offset.y > block.y && offset.y < block.y + size){
                    wasClicked = true
                    block.copy(visible = false)
                } else {
                    block
                }
            }
        }

        for (i in 0..<_blocks.value.size-1){
            if(_blocks.value[i].visible && !_blocks.value[i+1].visible){
                fail = true
            }
        }
    }

}
data class Blocks(
    var x: Float,
    var y: Float,
    val color: Color,
    val visible: Boolean
)