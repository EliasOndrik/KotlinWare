package com.example.kotlinware.ui.minigames

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinware.data.GameInterfaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class AbstractManagerViewModel(
    val gameRepository: GameInterfaceRepository
) : ViewModel() {
    abstract val minigameName: String
    private val _gameProgress = MutableStateFlow(GameProgress())
    val gameProgress = _gameProgress.asStateFlow()
    private val _currentMinigame = MutableStateFlow(MinigameType.TRANSITION)
    val currentMinigame = _currentMinigame.asStateFlow()
    private val _previousTimeMillis = MutableStateFlow(0L)
    val previousTimeMillis = _previousTimeMillis.asStateFlow()
    private val _timerMillis = MutableStateFlow(MinigameType.TRANSITION.time)
    val timerMillis = _timerMillis.asStateFlow()
    private var success = false
    private var saved = false

    fun switchMinigameType(type: MinigameType){
        _currentMinigame.update { type }
    }
    fun updateTimeMillis(time: Long){
        if (_gameProgress.value.lives<=0){
            if (!saved){
                saveProgress(minigameName)
                saved = true
            }
            return
        }
        if (_previousTimeMillis.value <= 0L){
            initializeTimer(time)
        }
        _timerMillis.update { it - (time - _previousTimeMillis.value) }
        _previousTimeMillis.update { time }
        if (_timerMillis.value<= 0L){
            if (_currentMinigame.value == MinigameType.TRANSITION){
                pickRandomMinigame()
                resetMinigame()

            } else {
                onGameEnded()
            }
            resetTimer(_currentMinigame.value.time)
        }
    }
    fun resetTimer(time: Long){
        _timerMillis.value = time
    }
    abstract fun pickRandomMinigame()

    fun onGameSuccess(){
        success = true
        resetTimer(_timerMillis.value.coerceAtMost(1000L))
    }
    fun onGameFail(){
        success = false
        resetTimer(_timerMillis.value.coerceAtMost(1000L))
    }
    fun onGameRetry(){
        initializeTimer(0L)
        _gameProgress.update { GameProgress() }
        _currentMinigame.value = MinigameType.TRANSITION
        success = false
        saved = false
    }

    fun onGameEnded(){
        if (success){
            _gameProgress.update { it.copy(score = it.score + 1) }
        } else {
            _gameProgress.update { it.copy(lives = it.lives - 1, score = it.score + 1) }
        }
        _currentMinigame.value = MinigameType.TRANSITION
        success = false
    }
    private fun initializeTimer(time: Long){
        _previousTimeMillis.update { time }
    }
    private fun saveProgress(minigameName: String){
        if(_gameProgress.value.lives>0){
            return
        }
        viewModelScope.launch {
            val currentMinigame = gameRepository.getMinigameByName(minigameName).first()
            if (_gameProgress.value.score > currentMinigame.score){
                val updateMinigame = currentMinigame.copy(score = _gameProgress.value.score)
                gameRepository.updateMinigame(updateMinigame)
            }
            val currentPlayer = gameRepository.getPlayer().first()
            val updatePlayer = currentPlayer.copy(money = currentPlayer.money + _gameProgress.value.score)
            gameRepository.updatePlayer(updatePlayer)
        }
    }
    abstract fun resetMinigame()
}
data class GameProgress(
    val lives: Int = 4,
    val score: Int = 0,
)
enum class MinigameType(
    val time: Long
){
    TRANSITION(2000L),
    BALLONPOP(5000L),
    CATTAP(5000L),
    PENWASTE(5000L),
    CORRECTORDER(5000L),
    WAVEGOODBYE(5000L),
    FOOTBALLSKILL(5000L),
    TUNEFINDER(5000L),
    PLATYPUSPARRY(5000L),
    CASHIN(6000L),
    DROPINBUCKET(5000L),
    LIGHTCANDLE(6000L),
    SUGARRUSH(5000L),
    LOVEMENOT(8000L),
    CAGEMATCH(5000L),
}