package com.example.kotlinware.ui.minigames.draging

import com.example.kotlinware.data.GameInterfaceRepository
import com.example.kotlinware.ui.minigames.AbstractManagerViewModel
import com.example.kotlinware.ui.minigames.MinigameType
import kotlin.random.Random

class DraggingGamesViewModel(gameRepository: GameInterfaceRepository) :
    AbstractManagerViewModel(gameRepository) {

    override val minigameName: String = "dragging"
    val cashInViewModel = CashInViewModel()
    val dropInBucketViewModel = DropInBucketViewModel()
    val lightCandleViewModel = LightCandleViewModel()

    override fun pickRandomMinigame() {
        val randomIndex = 2//Random.nextInt(4)
        when(randomIndex){
            0 -> {switchMinigameType(MinigameType.CASHIN )}
            1 -> {switchMinigameType(MinigameType.DROPINBUCKET)}
            2 -> {switchMinigameType(MinigameType.LIGHTCANDLE)}
            //3 -> {switchMinigameType(MinigameType.CORRECTORDER)}
            else -> {}
        }
    }

    override fun resetMinigame() {
        when(currentMinigame.value){
            MinigameType.CASHIN -> {cashInViewModel.resetMinigame()}
            MinigameType.DROPINBUCKET -> {dropInBucketViewModel.resetMinigame()}
            MinigameType.LIGHTCANDLE -> {lightCandleViewModel.resetMinigame()}
            //MinigameType.CORRECTORDER -> {correctOrderViewModel.resetMinigame()}
            else -> {}
        }
    }
}