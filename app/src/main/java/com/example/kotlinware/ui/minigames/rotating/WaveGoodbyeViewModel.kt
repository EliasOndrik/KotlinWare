package com.example.kotlinware.ui.minigames.rotating


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WaveGoodbyeViewModel: MinigameInterface{
    private val _leavingSize = MutableStateFlow(Size(200f,400f))
    val leavingSize = _leavingSize.asStateFlow()

    private val defaultSize = Size(200f,400f)
    private val distanceLength = 400f
    private var distance: Float = 400f
    private val _currentRotationZ= MutableStateFlow( 0f)
    val currentRotationZ = _currentRotationZ.asStateFlow()
    private var previousRotationZ: Float = 0f


    override fun resetMinigame() {
        _currentRotationZ.update { 0f }
        distance = distanceLength
        _leavingSize.update { defaultSize }
        previousRotationZ = 0f
    }

    override fun success(): Boolean {
        return distance<=0f
    }

    override fun fail(): Boolean {
        return false
    }

    override fun update(deltaTime: Long) {

    }
    fun update(rotations: FloatArray){
        if (previousRotationZ == 0f){
            previousRotationZ = rotations[0]
        }
        var delta = Math.toDegrees((rotations[0] - previousRotationZ).toDouble()).toFloat()

        if (delta > 180f) delta -= 360f
        if (delta < -180f) delta += 360f

        if (kotlin.math.abs(delta) > 0.15f) {
            var rotation = _currentRotationZ.value + delta
            if (rotation > 45f){
                rotation = 45f
            } else if (rotation < -45f){
                rotation = - 45f
            }

            val delta2 = rotation - _currentRotationZ.value
            distance -= kotlin.math.abs(delta2)
            if (distance<0f){
                distance = 0f
            }
            _leavingSize.update { it.copy(distance/2f, distance) }
            _currentRotationZ.update { rotation }

        }

        previousRotationZ = rotations[0]
    }

    override fun checkCollision(offset: Offset) {

    }

}