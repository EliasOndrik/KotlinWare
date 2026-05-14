package com.example.kotlinware.ui.minigames.rotating

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.Bullet
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class PlatypusParryViewModel: MinigameInterface {
    private var previousRotationZ = 0f
    private var currentRotationZ = 0f
    private val _bullets = MutableStateFlow(listOf(
        Bullet(-1f,-1f, active = false,false, 500f),
        Bullet(1f,-1f, active = false,false, 500f),
        Bullet(-1f,1f, active = false,false, 500f),
        Bullet(1f,1f, active = false,false, 500f),
    ))
    val bullets = _bullets.asStateFlow()
    private val _rotation = MutableStateFlow(0f)
    val rotation = _rotation.asStateFlow()

    private val bulletSpeed = 2f
    private var success = false
    private var failure = false

    private var previousTime = 0L

    override fun resetMinigame() {
        _bullets.update { currentList->
            currentList.map {bullet ->
                bullet.copy(active = false, distance = 500f, used = false)
            }
        }
        _rotation.update { 0f }
        previousRotationZ = 0f
        currentRotationZ = 0f
        success = false
        failure = false
        previousTime = 0L
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
        if (previousTime == 0L){
            previousTime = deltaTime
        }
        if (previousTime- deltaTime >= 1000L){
            val randomBullet = Random.nextInt(4)
            _bullets.update { currentList ->
                currentList.toMutableList().apply {
                    if (!this[randomBullet].used){
                        this[randomBullet] = this[randomBullet].copy(active = true)
                    }
                }
            }
            previousTime = deltaTime
        }
        if (deltaTime<100L){
            success = true
        }
    }

    fun update(rotations: FloatArray){
        if (fail()){
            return
        }
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
            currentRotationZ= rotation
        }

        _rotation.update { it + currentRotationZ/5 }
        if (_rotation.value>180f){
            _rotation.update { it - 360f }
        }
        if (rotation.value<-180f){
            _rotation.update { it + 360f }
        }
        previousRotationZ = rotations[0]
        updateBullets()
    }

    fun updateBullets(){
        _bullets.update { currentList->
            currentList.map { bullet ->
                if (bullet.active){
                    val degree = Math.toDegrees(kotlin.math.atan2(bullet.y,bullet.x).toDouble()).toFloat()
                    var active = true
                    var used = false
                    if ((degree+5f>=_rotation.value && degree-5f<=_rotation.value)&& bullet.distance<=250f){
                        active = false
                        used = true
                    }
                    if (bullet.distance<=100f){
                        failure = true
                    }
                    bullet.copy(distance = bullet.distance-bulletSpeed, active = active, used = used)
                }else{
                    bullet
                }
            }
        }

    }

    override fun checkCollision(offset: Offset) {
    }
}
data class Bullet(
    var x: Float,
    var y: Float,
    var active: Boolean,
    var used: Boolean,
    var distance: Float
)