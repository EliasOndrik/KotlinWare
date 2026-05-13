package com.example.kotlinware.ui.minigames.rotating

import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class FootballSkillViewModel: MinigameInterface {
    private val _leftLeg = MutableStateFlow(0f)
    val leftLeg = _leftLeg.asStateFlow()

    private val _rightLeg = MutableStateFlow(0f)
    val rightLeg = _rightLeg.asStateFlow()

    private val _ballOffset = MutableStateFlow(Offset(0f,-800f))
    val ballOffset = _ballOffset.asStateFlow()

    private var previousRotationZ: Float = 0f
    private var currentRotationZ: Float = 0f
    private var success = false
    private var failure = false
    private val gravity = 0.8f
    private val jumpForce = 36f

    private var force = 0f
    private var direction = BallDirection.RIGHT

    override fun resetMinigame() {
        _rightLeg.update { 0f }
        _leftLeg.update { 0f }
        _ballOffset.update { Offset(-225f,28f) }
        previousRotationZ = 0f
        currentRotationZ = 0f
        success = false
        failure = false
        force = -jumpForce
        direction = BallDirection.RIGHT
    }

    override fun success(): Boolean {
        return success
    }

    override fun fail(): Boolean {
        return failure
    }

    override fun update(deltaTime: Long) {
        if (fail()){
            return
        }
        if (deltaTime<=100){
            success = true
        }
        updateBall()
    }
    fun update(rotations: FloatArray){
        if (previousRotationZ == 0f){
            previousRotationZ = rotations[0]
        }
        var delta = Math.toDegrees((rotations[0] - previousRotationZ).toDouble()).toFloat()

        if (delta > 180f) delta -= 360f
        if (delta < -180f) delta += 360f

        if (kotlin.math.abs(delta) > 0.15f) {
            var rotation = currentRotationZ + delta
            if (rotation > 45f){
                rotation = 45f
            } else if (rotation < -45f){
                rotation = - 45f
            }

            currentRotationZ = rotation
            if (currentRotationZ >0f){
                _leftLeg.update { -currentRotationZ*2 }
                _rightLeg.update { 0f }
            }else{
                _rightLeg.update { -currentRotationZ*2 }
                _leftLeg.update { 0f }
            }
        }
        previousRotationZ = rotations[0]
    }
    private fun updateBall(){
        force += gravity
        _ballOffset.update { it.copy(x = it.x + direction.direction,y = it.y + force) }

        if (_ballOffset.value.y<100f && _ballOffset.value.y>0f){
            var collided = false
            if (_leftLeg.value<-45f && _ballOffset.value.x>0f){
                collided = true
            }
            if (_rightLeg.value>45f && _ballOffset.value.x<=0){
                collided = true
            }
            if (collided){
                force = -jumpForce
                direction = when(direction){
                    BallDirection.LEFT -> {
                        if(Random.nextBoolean()) BallDirection.RIGHT else BallDirection.STOP
                    }

                    BallDirection.RIGHT -> {
                        if(Random.nextBoolean()) BallDirection.LEFT else BallDirection.STOP
                    }

                    BallDirection.STOP -> {
                        if(_ballOffset.value.x<0) BallDirection.RIGHT else BallDirection.LEFT
                    }
                }
            }

        }

        if (_ballOffset.value.y>200f){
            failure = true
        }
    }
    override fun checkCollision(offset: Offset) {
    }
}
enum class BallDirection(
    val direction: Float
){
    LEFT(-5f),
    RIGHT(5f),
    STOP(0f)
}