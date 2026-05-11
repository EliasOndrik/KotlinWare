package com.example.kotlinware.ui.minigames.tapping

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PenWasteScreen(
    deltaTime: Long,
    onGameSuccess:()-> Unit,
    viewModel: PenWasteViewModel
){
    val penStart by viewModel.penStart.collectAsStateWithLifecycle()
    val penEnd by viewModel.penEnd.collectAsStateWithLifecycle()


    Canvas(
        modifier = Modifier.fillMaxSize().pointerInput(Unit){
            detectTapGestures { offset ->
                viewModel.checkCollision(offset - Offset(size.width/2f,size.height/2f))
                if (viewModel.success()){
                    onGameSuccess()
                }
            }
        }
    ) {
        viewModel.update(deltaTime)
        val screenWidth = size.width
        val screenHeight = size.height

        drawLine(
            color = Color.White,
            start = Offset( screenWidth/2f + penStart.x,screenHeight/2f + penStart.y),
            end = Offset(screenWidth/2f + penEnd.x,screenHeight/2f+ penEnd.y),
            strokeWidth = 5f
        )
        drawCircle(
            color = Color.Red,
            radius = viewModel.buttonRadius,
            center = Offset(screenWidth/2 + viewModel.buttonOffset.x,screenHeight/2 + viewModel.buttonOffset.y)
        )
    }
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text("Waste", fontSize = 40.sp)
        }
    }
}

@Preview
@Composable
fun PenWastePreview(){
    PenWasteScreen(0L,{},PenWasteViewModel())
}