package com.example.kotlinware.ui.minigames.swiping

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CageMatchViewModel: MinigameInterface {

    private val _animalOffset = MutableStateFlow(Offset(-100f,0f))
    val animalOffset = _animalOffset.asStateFlow()

    val animalSize = Size(200f,200f)

    private val _cageOffset = MutableStateFlow(Offset(-150f,-400f))
    val cageOffset = _cageOffset.asStateFlow()

    val cageSize = Size(300f,200f)

    private val _ropeStart = MutableStateFlow(Offset(0f,-400f))
    val ropeStart = _ropeStart.asStateFlow()
    private val _ropeEnd = MutableStateFlow(Offset(0f,-600f))
    val ropeEnd = _ropeEnd.asStateFlow()

    private var playerPosition = Offset(0f,0f)
    private var drop = false
    private val gravity = 10f
    private var animalDirection = 10f
    private var success = false
    private var onGround = false

    override fun resetMinigame() {
        _animalOffset.update { Offset(-100f,0f) }
        _cageOffset.update { Offset(-150f,-400f) }
        _ropeStart.update { Offset(0f,-400f) }
        _ropeEnd.update { Offset(0f,-600f) }
        drop = false
        success = false
        onGround = false
    }

    override fun success(): Boolean {
        return success
    }

    override fun fail(): Boolean {
        return onGround && !success
    }

    override fun update(deltaTime: Long) {
        if (onGround){
            return
        }
        _animalOffset.update { it.copy(x = it.x + animalDirection) }
        if (_animalOffset.value.x < -400f || _animalOffset.value.x + animalSize.width > 400f){
            animalDirection*=-1
        }
        if (drop){
            if (_cageOffset.value.y < _animalOffset.value.y){
                _cageOffset.update { it.copy(y = it.y+gravity) }
                _ropeStart.update { it.copy(y = it.y+gravity) }
                _ropeEnd.update { it.copy(y = it.y+gravity) }
            }else{
                onGround = true
            }

        }
        if (_cageOffset.value.x + cageSize.width >= _animalOffset.value.x && _cageOffset.value.x <= _animalOffset.value.x + animalSize.width
            && onGround){
            success = true

        }

    }

    override fun checkCollision(offset: Offset) {
        playerPosition = offset
    }
    fun update(dragAmount:Offset){
        val currentPosition = playerPosition
        playerPosition+=dragAmount
        if (currentPosition.y >= _ropeEnd.value.y && currentPosition.y <= _ropeStart.value.y &&
            ((currentPosition.x <=-5 && playerPosition.x>=5) || (currentPosition.x >=-5 && playerPosition.x<=5))){
            drop = true
            _ropeEnd.update { it.copy(y = playerPosition.y) }
        }
    }
}