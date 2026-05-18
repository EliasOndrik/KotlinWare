package com.example.kotlinware.ui.minigames.rotating

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinware.ui.AppViewModelProvider
import com.example.kotlinware.ui.minigames.Command
import com.example.kotlinware.ui.minigames.GameOverScreen
import com.example.kotlinware.ui.minigames.IntermissionScreen
import com.example.kotlinware.ui.minigames.MinigameType

@Composable
fun RotatingGamesScreen(
    onQuit: ()-> Unit,
    viewModel: RotatingGamesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val currentMinigame by viewModel.currentMinigame.collectAsStateWithLifecycle()
    val gameProgress by viewModel.gameProgress.collectAsStateWithLifecycle()
    val deltaTime by viewModel.timerMillis.collectAsStateWithLifecycle()
    val rotations by viewModel.rotationData.collectAsStateWithLifecycle()
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        viewModel.startListening()
        onDispose {
            viewModel.stopListening()
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
    LaunchedEffect(Unit) {
        while (true){
            withFrameMillis { frameTimeMillis ->
                viewModel.updateTimeMillis(frameTimeMillis)
            }
        }
    }
    Command(currentMinigame.commandId)
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

        MinigameType.WAVEGOODBYE -> {
            WaveGoodbyeScreen(
                onSuccess = {viewModel.onGameSuccess()},
                rotations,
                viewModel.waveGoodbyeViewModel
            )
        }

        MinigameType.FOOTBALLSKILL ->{
            FootballSkillScreen(
                onSuccess = {viewModel.onGameSuccess()},
                onFail = {viewModel.onGameFail()},
                rotations,
                deltaTime,
                viewModel.footballSkillViewModel
            )
        }
        MinigameType.TUNEFINDER -> {
            TuneFinderScreen(
                onSuccess = {viewModel.onGameSuccess()},
                rotations,
                viewModel.tuneFinderViewModel
            )
        }

        MinigameType.PLATYPUSPARRY -> {
            PlatypusParryScreen(
                onSuccess = {viewModel.onGameSuccess()},
                onFail = {viewModel.onGameFail()},
                rotations,
                deltaTime,
                viewModel.platypusParryViewModel
            )
        }

        else -> {}
    }

}
