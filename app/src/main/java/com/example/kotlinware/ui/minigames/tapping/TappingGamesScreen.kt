package com.example.kotlinware.ui.minigames.tapping

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinware.ui.AppViewModelProvider
import com.example.kotlinware.ui.minigames.GameOverScreen
import com.example.kotlinware.ui.minigames.IntermissionScreen
import com.example.kotlinware.ui.minigames.MinigameType
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun TappingGamesScreen(
    onQuit:()-> Unit,
    viewModel: TappingGamesViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
        MinigameType.TRANSITION -> {
            if (gameProgress.lives> 0){
                IntermissionScreen(gameProgress.lives,gameProgress.score)
            }else{
                GameOverScreen(
                    onQuit = onQuit,
                    onRetry = {viewModel.onGameRetry()},
                    score = gameProgress.score
                )
            }

        }
        MinigameType.BALLONPOP -> {
            BallonPopScreen(
                onGameSuccess = {viewModel.onGameSuccess()},
                viewModel.ballonPopViewModel
            )
        }
        MinigameType.CATTAP -> {
            CatTapScreen(
                deltaTime = deltaTime,
                onGameSuccess = {viewModel.onGameSuccess()},
                viewModel.catTapViewModel
            )
        }

        MinigameType.PENWASTE -> {
            PenWasteScreen(
                deltaTime = deltaTime,
                onGameSuccess = {viewModel.onGameSuccess()},
                viewModel.penWasteViewModel
            )
        }

        MinigameType.CORRECTORDER -> {
            CorrectOrderScreen(
                onGameSuccess = {viewModel.onGameSuccess()},
                onGameFail = {viewModel.onGameFail()},
                viewModel.correctOrderViewModel
            )
        }
    }
}


