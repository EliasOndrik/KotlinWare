package com.example.kotlinware.ui.minigames.swiping

import com.example.kotlinware.data.GameInterfaceRepository
import com.example.kotlinware.ui.minigames.AbstractManagerViewModel
import com.example.kotlinware.ui.minigames.MinigameType

class SwipingGamesViewModel(
    gameRepository: GameInterfaceRepository
) : AbstractManagerViewModel(gameRepository) {
    override val minigameName: String = "swiping"
    val loveMeNotViewModel = LoveMeNotViewModel()
    val cageMatchViewModel = CageMatchViewModel()
    val ballinViewModel = BallinViewModel()

    override fun pickRandomMinigame() {
        val randomIndex = 2//Random.nextInt(4)
        when(randomIndex){
            0 -> {switchMinigameType(MinigameType.LOVEMENOT )}
            1 -> {switchMinigameType(MinigameType.CAGEMATCH)}
            2 -> {switchMinigameType(MinigameType.BALLIN)}
            //3 -> {switchMinigameType(MinigameType.CORRECTORDER)}
            else -> {}
        }
    }

    override fun resetMinigame() {
        when(currentMinigame.value){
            MinigameType.LOVEMENOT -> {loveMeNotViewModel.resetMinigame()}
            MinigameType.CAGEMATCH -> {cageMatchViewModel.resetMinigame()}
            MinigameType.BALLIN -> {ballinViewModel.resetMinigame()}
            //MinigameType.CORRECTORDER -> {correctOrderViewModel.resetMinigame()}
            else -> {}
        }
    }
}