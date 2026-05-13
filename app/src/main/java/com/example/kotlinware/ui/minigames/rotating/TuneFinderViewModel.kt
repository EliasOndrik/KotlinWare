package com.example.kotlinware.ui.minigames.rotating

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class TuneFinderViewModel: MinigameInterface {
    private var previousRotationZ = 0f
    private val _currentRotationZ = MutableStateFlow(0f)
    val currentRotationZ = _currentRotationZ.asStateFlow()

    private val _radioColor = MutableStateFlow(Color.Red)
    val radioColor = _radioColor.asStateFlow()
    private val radioWaveLength = 10f
    private var radioWaveStart = 0f
    private var success = false

    override fun resetMinigame() {
        _radioColor.update { Color.Red }
        radioWaveStart = Random.nextInt(-90,80).toFloat()
        _currentRotationZ.update { 0f }
        previousRotationZ = 0f
        success = false
    }

    override fun success(): Boolean {
        return success
    }

    override fun fail(): Boolean {
        return false
    }

    override fun update(deltaTime: Long) {
    }

    fun update(rotations: FloatArray){
        if (success){
            return
        }
        if (previousRotationZ == 0f){
            previousRotationZ = rotations[0]
        }
        var delta = Math.toDegrees((rotations[0] - previousRotationZ).toDouble()).toFloat()

        if (delta > 180f) delta -= 360f
        if (delta < -180f) delta += 360f

        if (kotlin.math.abs(delta) > 0.15f) {
            var rotation = _currentRotationZ.value + delta
            if (rotation > 90f){
                rotation = 90f
            } else if (rotation < -90f){
                rotation = - 90f
            }
            _currentRotationZ.update { rotation }
        }

        if (_currentRotationZ.value >= radioWaveStart && _currentRotationZ.value <= radioWaveStart + radioWaveLength){
            _radioColor.update { Color.Green }
            success = true
        }
        previousRotationZ = rotations[0]
    }
    override fun checkCollision(offset: Offset) {
    }
}