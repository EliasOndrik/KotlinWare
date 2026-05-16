package com.example.kotlinware.ui.minigames.swiping

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameMillis
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinware.ui.AppViewModelProvider
import com.example.kotlinware.ui.minigames.GameOverScreen
import com.example.kotlinware.ui.minigames.IntermissionScreen
import com.example.kotlinware.ui.minigames.MinigameType
import com.example.kotlinware.ui.minigames.draging.CashInScreen
import com.example.kotlinware.ui.minigames.draging.DropInBucketScreen
import com.example.kotlinware.ui.minigames.draging.LightCandleScreen
import com.example.kotlinware.ui.minigames.draging.SugarRushScreen

@Composable
fun SwipingGamesScreen(
    onQuit: ()->Unit,
    viewModel: SwipingGamesViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
        MinigameType.LOVEMENOT->{
            LoveMeNotScreen(
                onSuccess = {viewModel.onGameSuccess()},
                deltaTime,
                viewModel.loveMeNotViewModel
            )
        }
        MinigameType.CAGEMATCH->{
            CageMatchScreen(
                onSuccess = {viewModel.onGameSuccess()},
                onFail = {viewModel.onGameFail()},
                deltaTime,
                viewModel.cageMatchViewModel
            )
        }
        MinigameType.BALLIN -> {
            BallinScreen(
                onSuccess = {viewModel.onGameSuccess()},
                deltaTime,
                viewModel.ballinViewModel
            )
        }

        MinigameType.PRISONESCAPE->{
            PrisonEscapeScreen(
                onSuccess = {viewModel.onGameSuccess()},
                viewModel.prisonEscapeViewModel
            )
        }
        else -> {}
    }
}