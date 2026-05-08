package com.example.kotlinware.ui.minigames.tapping

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TappingGamesViewModel : ViewModel(){
    private val _gameProgress = MutableStateFlow(GameProgress())
    val gameProgress = _gameProgress.asStateFlow()
    private val _currentMinigame = MutableStateFlow(TappingGames.TRANSITION)
    val currentMinigame = _currentMinigame.asStateFlow()
    private val _previousTimeMillis = MutableStateFlow(0L)
    val previousTimeMillis = _previousTimeMillis.asStateFlow()
    private val _timerMillis = MutableStateFlow(TappingGames.TRANSITION.time)
    val timerMillis = _timerMillis.asStateFlow()

    private var success = false

    fun updateTimeMillis(time: Long){
        _timerMillis.update { it - (time - _previousTimeMillis.value) }

        _previousTimeMillis.update { time }
        if (_timerMillis.value<= 0L){
            if (_currentMinigame.value == TappingGames.TRANSITION){
                pickRandomMinigame()
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
        _currentMinigame.update { TappingGames.BALLONPOP }
    }

    fun onGameSuccess(){
        success = true
        resetTimer(_timerMillis.value.coerceAtMost(1000L))
    }
    fun onGameFail(){
        success = false
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
}