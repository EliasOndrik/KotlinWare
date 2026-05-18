package com.example.kotlinware.ui.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinware.data.GameInterfaceRepository
import com.example.kotlinware.data.Minigame
import com.example.kotlinware.data.Player
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShopViewModel(
    private val gameRepository: GameInterfaceRepository
) : ViewModel() {
    
    val uiState : StateFlow<List<Minigame>> = gameRepository.getAllMinigames().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )
    fun onGameClick(minigame: Minigame){
        if (minigame.unlocked){
            return
        }
        viewModelScope.launch {
            val currentPlayer = gameRepository.getPlayer().first()
            if (currentPlayer.money >= 40){
                gameRepository.updateMinigame(minigame.copy(unlocked = true))
                val updatedPlayer = currentPlayer.copy(money = currentPlayer.money - 40)
                gameRepository.updatePlayer(updatedPlayer)
            }
        }
    }
}