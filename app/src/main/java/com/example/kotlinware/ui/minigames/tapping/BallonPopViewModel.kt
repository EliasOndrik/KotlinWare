package com.example.kotlinware.ui.minigames.tapping


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class BallonPopViewModel{

    private val _ballons = MutableStateFlow(listOf(
        Ballon(0f, 120f, BallonColor.BLUE.color,false),
        Ballon(-320f, -80f, BallonColor.RED.color,false),
        Ballon(320f, 200f, BallonColor.RED.color,false),
        Ballon(-300f, 200f, BallonColor.YELLOW.color,false),
        Ballon(0f, -100f, BallonColor.BLUE.color,false),
        Ballon(300f, -120f, BallonColor.GREEN.color,false),
    ))
    val ballons = _ballons.asStateFlow()
    fun checkBallonsCollision(offset: Offset){
        _ballons.update { currentList ->
            var wasPopped = false
            currentList.map { ballon ->
                val distance = (offset - Offset(ballon.x,ballon.y)).getDistance()
                if (wasPopped){
                    ballon
                } else if (!ballon.popped && distance <= 200f){
                    wasPopped = true
                    ballon.copy(popped = true)
                } else{
                    ballon
                }
            }
        }
    }
    fun areBallonsPopped(): Boolean{
        var arePopped = true
        for (i in 0..<_ballons.value.size){
            arePopped = arePopped && _ballons.value[i].popped
        }
        return arePopped
    }
    fun resetMinigame(){
        _ballons.update { currentList ->
            currentList.map { ballon ->
                ballon.copy(popped = false)
            }
        }
    }
}
data class Ballon(
    val x: Float,
    val y: Float,
    val color: Color,
    val popped: Boolean
)
enum class BallonColor(
    val color: Color
){
    RED(Color.Red),
    GREEN(Color.Green),
    BLUE(Color.Blue),
    YELLOW(Color.Yellow)
}