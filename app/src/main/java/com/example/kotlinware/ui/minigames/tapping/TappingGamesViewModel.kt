package com.example.kotlinware.ui.minigames.tapping

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class TappingGamesViewModel : ViewModel(){
    private val _gameProgress = MutableStateFlow(GameProgress())
    val gameProgress = _gameProgress.asStateFlow()
    private val _currentMinigame = MutableStateFlow(TappingGames.TRANSITION)
    val currentMinigame = _currentMinigame.asStateFlow()
    private val _previousTimeMillis = MutableStateFlow(0L)
    val previousTimeMillis = _previousTimeMillis.asStateFlow()
    private val _timerMillis = MutableStateFlow(TappingGames.TRANSITION.time)
    val timerMillis = _timerMillis.asStateFlow()
    val ballonPopViewModel = BallonPopViewModel()
    val catTapViewModel = CatTapViewModel()
    val penWasteViewModel = PenWasteViewModel()
    val correctOrderViewModel = CorrectOrderViewModel()

    private var success = false

    fun updateTimeMillis(time: Long){
        if (_previousTimeMillis.value <= 0L){
            initializeTimer(time)
        }
        _timerMillis.update { it - (time - _previousTimeMillis.value) }

        _previousTimeMillis.update { time }
        if (_timerMillis.value<= 0L){
            if (_currentMinigame.value == TappingGames.TRANSITION){
                pickRandomMinigame()

                when(_currentMinigame.value){
                    TappingGames.TRANSITION -> {}
                    TappingGames.BALLONPOP -> {ballonPopViewModel.resetMinigame()}
                    TappingGames.CATTAP -> {catTapViewModel.resetMinigame()}
                    TappingGames.PENWASTE -> {penWasteViewModel.resetMinigame()}
                    TappingGames.CORRECTORDER -> {correctOrderViewModel.resetMinigame()}
                }

            } else {
                onGameEnded()
            }
            resetTimer(_currentMinigame.value.time)
        }
    }
    fun resetTimer(time: Long){
        _timerMillis.value = time
    }
    fun pickRandomMinigame(){
        val randomIndex = Random.nextInt(5)
        when(randomIndex){
            0 ->{_currentMinigame.update { TappingGames.BALLONPOP } }
            1 -> {_currentMinigame.update { TappingGames.CATTAP }}
            2 -> {_currentMinigame.update { TappingGames.PENWASTE }}
            3 -> {_currentMinigame.update { TappingGames.CORRECTORDER }}
            else -> {}
        }

    }

    fun onGameSuccess(){
        success = true
        resetTimer(_timerMillis.value.coerceAtMost(1000L))
    }
    fun onGameFail(){
        success = false
        resetTimer(_timerMillis.value.coerceAtMost(1000L))
    }
    fun onGameEnded(){
        if (success){
            _gameProgress.update { it.copy(score = it.score + 1) }
        } else {
            _gameProgress.update { it.copy(lives = it.lives - 1, score = it.score + 1) }
        }
        _currentMinigame.value = TappingGames.TRANSITION
        success = false
    }
    private fun initializeTimer(time: Long){
        _previousTimeMillis.update { time }
    }
}

data class GameProgress(
    val lives: Int = 4,
    val score: Int = 0,
)

enum class TappingGames(
    val time: Long
){
    TRANSITION(2000L),
    BALLONPOP(5000L),
    CATTAP(5000L),
    PENWASTE(5000L),
    CORRECTORDER(5000L),
}