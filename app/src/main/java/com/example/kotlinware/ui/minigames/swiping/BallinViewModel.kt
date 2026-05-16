package com.example.kotlinware.ui.minigames.swiping

import androidx.compose.ui.geometry.Offset
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class BallinViewModel : MinigameInterface{

    private val _ballOffset = MutableStateFlow(Offset(0f,300f))
    val ballOffset = _ballOffset.asStateFlow()
    private val _ballRadius = MutableStateFlow(200f)
    val ballRadius = _ballRadius.asStateFlow()
    private val _basketOffset = MutableStateFlow(Offset(-100f,-400f))
    val basketOffset = _basketOffset.asStateFlow()

    private var basketDirection = Basket.CENTER
    private var ballDirection = Basket.CENTER
    private var selected = false
    private var thrown = false
    private var fall =false

    override fun resetMinigame() {
        _ballOffset.update { Offset(0f,300f) }
        _ballRadius.update { 200f }
        val rand = Random.nextInt(3)
        when(rand){
            0->{
                basketDirection = Basket.LEFT
                _basketOffset.update { Offset(-400f,-400f) }
            }
            1->{
                basketDirection = Basket.CENTER
                _basketOffset.update { Offset(-100f,-400f) }
            }
            2->{
                basketDirection = Basket.RIGHT
                _basketOffset.update { Offset(+200f,-400f) }
            }

        }
        selected = false
        thrown = false
        fall = false

    }

    override fun success(): Boolean {
        return thrown && ballDirection == basketDirection
    }


    override fun fail(): Boolean {
        return thrown && ballDirection != basketDirection
    }

    override fun update(deltaTime: Long) {
        if (thrown){
            if (_ballOffset.value.y<-300f){
                fall = true
            }
            if (fall&&success()){
                _ballOffset.update { it + Offset(0f,10f) }
            } else {
                _ballRadius.update { it - 2f }
                _ballOffset.update { it + ballDirection.offset }
            }

        }
    }

    override fun checkCollision(offset: Offset) {
        val distance = (offset - ballOffset.value).getDistance()
        if (distance<= ballRadius.value){
            selected = true
        }
    }

    fun update(dragAmount:Offset){
        if (!selected){
            return
        }
        if (thrown){
            return
        }
        val strength = dragAmount.getDistance()
        if (strength <= 30f){
            return
        }
        val angle = Math.toDegrees( kotlin.math.atan2(dragAmount.y,dragAmount.x).toDouble()).toFloat()
        when (angle){
            in -135f..<-105f->{
                ballDirection = Basket.LEFT
                thrown = true
            }
            in -105f..<-75f->{
                ballDirection = Basket.CENTER
                thrown = true
            }
            in -75f..-45f->{
                ballDirection = Basket.RIGHT
                thrown = true
            }
        }
    }
    fun drop(){
        selected =false
    }


}
enum class Basket(
    val offset: Offset,
){
    LEFT(Offset(-5f,-10f)),
    CENTER(Offset(0f,-10f)),
    RIGHT(Offset(5f,-10f)),
}