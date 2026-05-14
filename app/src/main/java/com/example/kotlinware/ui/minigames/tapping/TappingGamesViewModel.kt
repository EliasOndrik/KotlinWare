package com.example.kotlinware.ui.minigames.tapping

import com.example.kotlinware.data.GameInterfaceRepository
import com.example.kotlinware.ui.minigames.AbstractManagerViewModel
import com.example.kotlinware.ui.minigames.MinigameType
import kotlin.random.Random

class TappingGamesViewModel(
    gameRepository: GameInterfaceRepository
) : AbstractManagerViewModel(gameRepository){

    override val minigameName: String = "tapping"

    val ballonPopViewModel = BallonPopViewModel()
    val catTapViewModel = CatTapViewModel()
    val penWasteViewModel = PenWasteViewModel()
    val correctOrderViewModel = CorrectOrderViewModel()


    override fun pickRandomMinigame(){
        val randomIndex = Random.nextInt(4)
        when(randomIndex){
            0 -> {switchMinigameType(MinigameType.BALLONPOP )}
            1 -> {switchMinigameType(MinigameType.CATTAP)}
            2 -> {switchMinigameType(MinigameType.PENWASTE)}
            3 -> {switchMinigameType(MinigameType.CORRECTORDER)}
            else -> {}
        }
    }

    override fun resetMinigame(){
        when(currentMinigame.value){
            MinigameType.BALLONPOP -> {ballonPopViewModel.resetMinigame()}
            MinigameType.CATTAP -> {catTapViewModel.resetMinigame()}
            MinigameType.PENWASTE -> {penWasteViewModel.resetMinigame()}
            MinigameType.CORRECTORDER -> {correctOrderViewModel.resetMinigame()}
            else -> {}
        }
    }
}


/*enum class TappingGames(
    val time: Long
){
    TRANSITION(2000L),
    BALLONPOP(5000L),
    CATTAP(5000L),
    PENWASTE(5000L),
    CORRECTORDER(5000L),
}*/