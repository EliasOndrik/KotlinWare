package com.example.kotlinware.ui.minigames.tapping

import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random


class PenWasteViewModel {

    private val _penStart = MutableStateFlow(Offset(0f,0f))
    val penStart = _penStart.asStateFlow()
    private val _penEnd = MutableStateFlow(Offset(0f,0f))
    val penEnd = _penEnd.asStateFlow()
    val buttonOffset: Offset = Offset(200f,-200f)
    val buttonRadius: Float = 40f
    var numberOfClicks: Int = 0
    val clickLimit: Int = 20
    val fallSpeed: Float = 20f
    val ground: Float = 400f

    fun update(deltaTime: Long){
        if (!success()){
            return
        }
        if (_penStart.value.y <= ground){
            _penStart.update { it.copy(y = it.y + fallSpeed) }
        }
        if (_penEnd.value.y <= ground){
            _penEnd.update { it.copy(y = it.y + fallSpeed) }
        }


    }

    fun checkCollision(offset: Offset){
        val distance = (offset - Offset(buttonOffset.x,buttonOffset.y)).getDistance()
        if (distance <= buttonRadius && numberOfClicks <= clickLimit){
            numberOfClicks++
            _penStart.update { it.copy(x = -numberOfClicks*10f, y = +numberOfClicks*10f) }
        }

    }

    fun resetMinigame(){
        numberOfClicks = Random.nextInt(10)
        _penStart.update { it.copy(x = -numberOfClicks*10f, y = +numberOfClicks*10f) }
        _penEnd.update { it.copy(x = 0f, y = 0f) }
    }

    fun success(): Boolean{
        return numberOfClicks >= clickLimit
    }
}

