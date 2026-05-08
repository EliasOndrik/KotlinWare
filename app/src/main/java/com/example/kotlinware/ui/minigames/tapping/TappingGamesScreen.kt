package com.example.kotlinware.ui.minigames.tapping

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameMillis
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinware.ui.minigames.IntermissionScreen

@Composable
fun TappingGamesScreen(
    viewModel: TappingGamesViewModel = viewModel()
){
    val currentMinigame by viewModel.currentMinigame.collectAsStateWithLifecycle()
    val gameProgress by viewModel.gameProgress.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {

        while (true){
            withFrameMillis { frameTimeMillis ->
                viewModel.updateTimeMillis(frameTimeMillis)
            }
        }
    }
    when (currentMinigame){
        TappingGames.TRANSITION -> {
            IntermissionScreen(gameProgress.lives,gameProgress.score)
        }
        TappingGames.BALLONPOP -> {
            BallonPopScreen(
                onGameSuccess = {viewModel.onGameSuccess()},
            )
        }
    }
}


