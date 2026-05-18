package com.example.kotlinware.ui.minigames.rotating

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinware.data.GameInterfaceRepository
import com.example.kotlinware.ui.minigames.GameProgress
import com.example.kotlinware.ui.minigames.MinigameType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class RotatingGamesViewModel(
    application: Application,
    val gameRepository: GameInterfaceRepository
) : AndroidViewModel(application) {
    val waveGoodbyeViewModel = WaveGoodbyeViewModel()
    val footballSkillViewModel = FootballSkillViewModel()
    val tuneFinderViewModel = TuneFinderViewModel()
    val platypusParryViewModel = PlatypusParryViewModel()
    private val sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)

    private val _rotationData = MutableStateFlow(FloatArray(3))
    val rotationData = _rotationData.asStateFlow()
    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_GAME_ROTATION_VECTOR) {
                val rotationMatrix = FloatArray(9)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                val orientation = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientation)

                _rotationData.update { orientation }
            }
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    fun startListening() {
        rotationSensor?.let {
            sensorManager.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(sensorListener)
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
    }
// Nedokáže dediť od androidViewModel a abstractManager naraz.
    val minigameName: String = "rotating"
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
    fun pickRandomMinigame() {
        val randomIndex = Random.nextInt(4)
        when(randomIndex){
            0 -> {switchMinigameType(MinigameType.WAVEGOODBYE )}
            1 -> {switchMinigameType(MinigameType.FOOTBALLSKILL)}
            2 -> {switchMinigameType(MinigameType.TUNEFINDER)}
            3 -> {switchMinigameType(MinigameType.PLATYPUSPARRY)}
            else -> {}
        }
    }

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
    fun resetMinigame(){
        when(currentMinigame.value){
            MinigameType.WAVEGOODBYE -> {waveGoodbyeViewModel.resetMinigame()}
            MinigameType.FOOTBALLSKILL->{footballSkillViewModel.resetMinigame()}
            MinigameType.TUNEFINDER->{tuneFinderViewModel.resetMinigame()}
            MinigameType.PLATYPUSPARRY->{platypusParryViewModel.resetMinigame()}
            else -> {}
        }
    }
}