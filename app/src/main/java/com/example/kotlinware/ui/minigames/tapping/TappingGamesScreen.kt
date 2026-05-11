package com.example.kotlinware.ui.minigames.tapping

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinware.ui.minigames.IntermissionScreen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun TappingGamesScreen(
    viewModel: TappingGamesViewModel = viewModel()
){
    val currentMinigame by viewModel.currentMinigame.collectAsStateWithLifecycle()
    val gameProgress by viewModel.gameProgress.collectAsStateWithLifecycle()
    val deltaTime by viewModel.timerMillis.collectAsStateWithLifecycle()
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
                viewModel.ballonPopViewModel
            )
        }
        TappingGames.CATTAP -> {
            CatTapScreen(
                deltaTime = deltaTime,
                onGameSuccess = {viewModel.onGameSuccess()},
                viewModel.catTapViewModel
            )
        }

        TappingGames.PENWASTE -> {
            PenWasteScreen(
                deltaTime = deltaTime,
                onGameSuccess = {viewModel.onGameSuccess()},
                viewModel.penWasteViewModel
            )
        }
    }
}


