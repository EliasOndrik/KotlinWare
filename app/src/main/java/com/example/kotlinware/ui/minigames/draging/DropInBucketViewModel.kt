package com.example.kotlinware.ui.minigames.draging

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.withIndex
import kotlin.random.Random

class DropInBucketViewModel: MinigameInterface {

    private val _waterDrops = MutableStateFlow(listOf(
        Water(0f,0f, collected = false, force = 0f, started = false),
        Water(0f,0f, collected = false, force = 0f, started = false),
        Water(0f,0f, collected = false, force = 0f, started = false),
        Water(0f,0f, collected = false, force = 0f, started = false),
    ))
    val waterDrops = _waterDrops.asStateFlow()
    private val gravity = 0.8f
    private var success = false
    private var failure = false
    private var previousTimer = 0L
    val bucketSize = Size(200f,100f)
    private val _bucketOffset = MutableStateFlow(Offset(-100f,200f))
    val bucketOffset = _bucketOffset.asStateFlow()
    private val edge = 400f

    override fun resetMinigame() {
        _waterDrops.update { currentList->
            currentList.map { water ->
                water.copy(x = Random.nextInt(-edge.toInt(),edge.toInt()).toFloat(),
                    y = -500f, collected = false, force = 0f, started = false)
            }
        }
        success = false
        failure = false
        previousTimer = 0L
        _bucketOffset.update { it.copy(x = -bucketSize.width/2, y = 200f) }
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
        if (previousTimer <= 0L){
            previousTimer = deltaTime
        }
        if (previousTimer- deltaTime >= 1000L){
            val random = Random.nextInt(4)
            _waterDrops.update { currentList->
                currentList.toMutableList().apply{
                    if (!this[random].collected){
                        this[random] = this[random].copy(started = true)
                    }
                }
            }
            previousTimer = deltaTime
        }
        if (deltaTime<100L){
            success = true
        }
        updateWater()


    }


    override fun checkCollision(offset: Offset) {

    }

    private fun updateWater(){
        _waterDrops.update { currentList->
            currentList.toMutableList().apply {
                for (i in 0..<this.size){
                    if (!this[i].started){
                        continue
                    }
                    this[i] = this[i].copy(force = this[i].force + gravity, y = this[i].force + this[i].y)
                    if (this[i].x >= bucketOffset.value.x && this[i].x <= bucketOffset.value.x + bucketSize.width &&
                        this[i].y >= bucketOffset.value.y && this[i].y <= bucketOffset.value.y + bucketSize.height){
                        this[i] = this[i].copy(collected = true, started = false)

                    }
                    if (this[i].y>400f){
                        failure = true
                    }
                }
            }
        }
    }
    fun updateBucket(dragAmount: Offset){
        var correction = _bucketOffset.value.x + dragAmount.x
        if (correction < -edge ){
            correction = -edge
        }
        if (correction > edge - bucketSize.width){
            correction = edge - bucketSize.width
        }
        _bucketOffset.update { it.copy(x = correction) }
    }
}

data class Water(
    var x: Float,
    var y: Float,
    var force : Float,
    var collected: Boolean,
    var started: Boolean
)