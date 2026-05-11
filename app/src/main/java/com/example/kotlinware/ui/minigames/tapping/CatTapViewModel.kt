package com.example.kotlinware.ui.minigames.tapping

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class CatTapViewModel {
    val width: Float = 150f
    val height: Float = 400f
    val distanceBetween: Float = 180f
    val movementSpeed: Float = 10f
    var correctCat : Int = 0
    var success: Boolean = false
    var turnAround: Boolean = false
    var jumpTimer: Int = 0
    private val _cats = MutableStateFlow(listOf(
        Cat(-width/2f - 2 * distanceBetween,-height,CatState.WAITING),
        Cat(-width/2f - distanceBetween,-height,CatState.WAITING),
        Cat(-width/2f,-height,CatState.WAITING),
        Cat(-width/2f + distanceBetween,-height,CatState.WAITING),
        Cat(-width/2f + 2* distanceBetween,-height,CatState.WAITING),
    )
    )
    val cats = _cats.asStateFlow()
    fun update(
        deltaTime: Long
    ){
        if (success){
            return
        }
        when(_cats.value[correctCat].catState){
            CatState.WAITING -> {
                if (jumpTimer > deltaTime){
                    _cats.update { currentList->
                        currentList.toMutableList().apply {
                            this[correctCat] = this[correctCat].copy(catState = CatState.PEEKING)
                        }
                    }
                }
            }
            CatState.PEEKING -> {
                if (_cats.value[correctCat].y>=0f){
                    turnAround = true
                }
                _cats.update { currentList->
                    currentList.toMutableList().apply {
                        if (turnAround){
                            this[correctCat] = this[correctCat].copy(y = this[correctCat].y - movementSpeed)
                        } else {
                            this[correctCat] = this[correctCat].copy(y = this[correctCat].y + movementSpeed)
                        }
                        if (this[correctCat].y<-height){
                            this[correctCat] = this[correctCat].copy(catState = CatState.WAITING, y = -height)
                            turnAround = false
                            jumpTimer = Random.nextInt(4000)+1000
                            correctCat = Random.nextInt(5)
                        }
                    }
                }
            }
        }

    }
    fun checkCollision(offset: Offset){
        val cat = _cats.value[correctCat]
        if (offset.x > cat.x && offset.x < cat.x + width && offset.y > 0 && offset.y < cat.y + height){
            success = true
        }
    }
    fun resetMinigame(){
        success = false
        turnAround = false
        jumpTimer = Random.nextInt(4000)+1000
        correctCat = Random.nextInt(5)
        _cats.update { currentList ->
            var i = -3
            currentList.map { cat->
                i++
                cat.copy(x = -width/2f +i*distanceBetween, y = -height, catState = CatState.WAITING)
            }
        }
    }
    fun success(): Boolean{
        return success
    }

}

data class Cat(
    var x: Float,
    var y: Float,
    var catState: CatState
)
enum class CatState{
    WAITING,
    PEEKING,
}
