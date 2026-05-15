package com.example.kotlinware.ui.minigames.draging

import androidx.compose.ui.geometry.Offset
import com.example.kotlinware.ui.minigames.MinigameInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class SugarRushViewModel: MinigameInterface {

    private val _candies = MutableStateFlow(listOf(
        Candy(0f,0f,false),
        Candy(0f,0f,false),
        Candy(0f,0f,false),
        Candy(0f,0f,false),
    ))
    val candies = _candies.asStateFlow()
    private val _tongueEnd = MutableStateFlow(Offset(0f,0f))
    val tongueEnd = _tongueEnd.asStateFlow()

    private var isBeingUsed = false
    private var ended = false
    private val defaultEndOffset = Offset(-200f,100f)

    override fun resetMinigame() {
        _candies.update {
            it.map {candy ->
                candy.copy(x = Random.nextInt(-400,400).toFloat(), y = Random.nextInt(-400,0).toFloat(), collected = false)
            }
        }
        _tongueEnd.update { defaultEndOffset }
        isBeingUsed = false
        ended = false
    }

    override fun success(): Boolean {
        var success = true
        candies.value.forEach {
            success = success && it.collected
        }
        return success
    }

    override fun fail(): Boolean {
        return false
    }

    override fun update(deltaTime: Long) {
        _candies.update {
            it.map {candy ->
                if (candy.collected){
                    candy.copy(x = _tongueEnd.value.x, y = _tongueEnd.value.y)
                }else{
                    candy
                }
            }
        }
    }

    override fun checkCollision(offset: Offset) {
        val distance = (offset - Offset(_tongueEnd.value.x,_tongueEnd.value.y)).getDistance()
        if (distance <= 100f){
            isBeingUsed = true
        }
    }
    fun update(dragAmount: Offset){
        if (!isBeingUsed){
            return
        }
        _tongueEnd.update { it.copy(x = it.x +dragAmount.x, y = it.y + dragAmount.y)}
        _candies.update {
            it.map { candy ->
                val distance = (Offset(candy.x,candy.y) - Offset(_tongueEnd.value.x,_tongueEnd.value.y)).getDistance()
                if (distance <= 100f){
                    candy.copy(collected = true)
                } else {
                    candy
                }
            }
        }

    }
    fun dropIt(){
        _tongueEnd.update { it.copy(x = defaultEndOffset.x, y = defaultEndOffset.y) }
        isBeingUsed = false
    }

    fun isDone(): Boolean{
        return ended
    }
    fun done(){
        ended = true
    }
}

data class Candy(
    var x: Float,
    var y: Float,
    var collected: Boolean
)