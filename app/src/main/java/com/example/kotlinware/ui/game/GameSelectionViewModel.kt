package com.example.kotlinware.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinware.data.GameInterfaceRepository
import com.example.kotlinware.data.Minigame
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class GameSelectionViewModel(
    gameRepository: GameInterfaceRepository
) : ViewModel() {
    val playableGameState : StateFlow<List<Minigame>> = gameRepository.getAllPlayableMinigames().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )

}