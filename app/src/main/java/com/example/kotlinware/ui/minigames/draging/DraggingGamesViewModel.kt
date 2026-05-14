package com.example.kotlinware.ui.minigames.draging

import com.example.kotlinware.data.GameInterfaceRepository
import com.example.kotlinware.ui.minigames.AbstractManagerViewModel
import com.example.kotlinware.ui.minigames.MinigameType
import kotlin.random.Random

class DraggingGamesViewModel(gameRepository: GameInterfaceRepository) :
    AbstractManagerViewModel(gameRepository) {

    override val minigameName: String = "dragging"
    val cashInViewModel = CashInViewModel()

    override fun pickRandomMinigame() {
        val randomIndex = 0//Random.nextInt(4)
        when(randomIndex){
            0 -> {switchMinigameType(MinigameType.CASHIN )}
            //1 -> {switchMinigameType(MinigameType.CATTAP)}
            //2 -> {switchMinigameType(MinigameType.PENWASTE)}
            //3 -> {switchMinigameType(MinigameType.CORRECTORDER)}
            else -> {}
        }
    }

    override fun resetMinigame() {
        when(currentMinigame.value){
            MinigameType.CASHIN -> {cashInViewModel.resetMinigame()}
            //MinigameType.CATTAP -> {catTapViewModel.resetMinigame()}
            //MinigameType.PENWASTE -> {penWasteViewModel.resetMinigame()}
            //MinigameType.CORRECTORDER -> {correctOrderViewModel.resetMinigame()}
            else -> {}
        }
    }
}