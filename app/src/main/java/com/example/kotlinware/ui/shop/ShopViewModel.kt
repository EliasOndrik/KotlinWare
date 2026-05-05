package com.example.kotlinware.ui.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinware.data.GameInterfaceRepository
import com.example.kotlinware.data.Minigame
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ShopViewModel(
    gameRepository: GameInterfaceRepository
) : ViewModel() {

    val uiState : StateFlow<List<Minigame>> = gameRepository.getAllMinigames().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )
}