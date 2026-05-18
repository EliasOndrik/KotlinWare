package com.example.kotlinware.ui.minigames

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinware.R
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
        _timerMillis.update {time}
    }
    abstract fun pickRandomMinigame()

    fun onGameSuccess(){
        if (currentMinigame.value == MinigameType.TRANSITION){
            return
        }
        success = true
        resetTimer(_timerMillis.value.coerceAtMost(1000L))
    }
    fun onGameFail(){
        if (currentMinigame.value == MinigameType.TRANSITION){
            return
        }
        success = false
        resetTimer(_timerMillis.value.coerceAtMost(1000L))
    }
    fun onGameRetry(){
        initializeTimer(0L)
        _gameProgress.update { GameProgress() }
        _currentMinigame.update {MinigameType.TRANSITION}
        success = false
        saved = false
    }

    fun onGameEnded(){
        if (success){
            _gameProgress.update { it.copy(score = it.score + 1) }
        } else {
            _gameProgress.update { it.copy(lives = it.lives - 1, score = it.score + 1) }
        }
        _currentMinigame.update { MinigameType.TRANSITION }
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
    val time: Long,
    val commandId: Int
){
    TRANSITION(2000L,R.string.transition),
    BALLONPOP(5000L,R.string.ballonPop),
    CATTAP(5000L, R.string.catTap),
    PENWASTE(5000L, R.string.penWaste),
    CORRECTORDER(5000L, R.string.correctOrder),
    WAVEGOODBYE(5000L, R.string.waveGoodbye),
    FOOTBALLSKILL(5000L, R.string.footballSkill),
    TUNEFINDER(5000L, R.string.tuneFinder),
    PLATYPUSPARRY(5000L, R.string.platypusParry),
    CASHIN(6000L, R.string.cashIn),
    DROPINBUCKET(5000L, R.string.dropInBucket),
    LIGHTCANDLE(6000L, R.string.lightCandle),
    SUGARRUSH(5000L, R.string.sugarRush),
    LOVEMENOT(8000L, R.string.loveMeNot),
    CAGEMATCH(5000L, R.string.cageMatch),
    BALLIN(5000L, R.string.ballin),
    PRISONESCAPE(6000L, R.string.prisonEscape)
}