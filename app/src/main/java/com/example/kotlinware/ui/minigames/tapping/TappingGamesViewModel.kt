package com.example.kotlinware.ui.minigames.tapping


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinware.data.GameInterfaceRepository
import com.example.kotlinware.data.Minigame
import com.example.kotlinware.data.Player
import com.example.kotlinware.ui.minigames.GameProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class TappingGamesViewModel(
    private val gameRepository: GameInterfaceRepository
) : ViewModel(){

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
        if (_gameProgress.value.lives<=0){
            return
        }
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
    fun onGameRetry(){

        initializeTimer(0L)
        _gameProgress.update { GameProgress() }
        _currentMinigame.value = TappingGames.TRANSITION
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
    private fun initializeTimer(time: Long){
        _previousTimeMillis.update { time }
    }
    fun saveProgress(){
        if(_gameProgress.value.lives>0){
            return
        }
        viewModelScope.launch {
            val currentMinigame = gameRepository.getMinigameByName("tapping").first()
            if (_gameProgress.value.score > currentMinigame.score){
                val updateMinigame = currentMinigame.copy(score = _gameProgress.value.score)
                gameRepository.updateMinigame(updateMinigame)
            }
            val currentPlayer = gameRepository.getPlayer().first()
            val updatePlayer = currentPlayer.copy(money = currentPlayer.money + _gameProgress.value.score)
            gameRepository.updatePlayer(updatePlayer)
        }
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