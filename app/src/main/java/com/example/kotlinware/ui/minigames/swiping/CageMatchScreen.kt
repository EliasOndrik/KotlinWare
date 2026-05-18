package com.example.kotlinware.ui.minigames.swiping

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CageMatchScreen(
    onSuccess:()->Unit,
    onFail:()->Unit,
    deltaTime:Long,
    viewModel: CageMatchViewModel
){
    val ropeStart by viewModel.ropeStart.collectAsStateWithLifecycle()
    val ropeEnd by viewModel.ropeEnd.collectAsStateWithLifecycle()
    val animalOffset by viewModel.animalOffset.collectAsStateWithLifecycle()
    val cageOffset by viewModel.cageOffset.collectAsStateWithLifecycle()

    Canvas(
        modifier = Modifier.fillMaxSize().pointerInput(Unit){
            detectDragGestures(
                onDragStart = {offset->
                    viewModel.checkCollision(offset - Offset(size.width/2f,size.height/2f))
                },
                onDrag = {change, dragAmount ->
                    change.consume()
                    viewModel.update(dragAmount)
                },
                onDragEnd = {
                }
            )
        }
    ) {
        viewModel.update(deltaTime)
        if (viewModel.success()){
            onSuccess()
        }
        if(viewModel.fail()){
            onFail()
        }
        val screenWidth = size.width
        val screenHeight = size.height
        drawRect(
            color = Color.Gray,
            size = Size(300f,200f),
            topLeft = Offset( screenWidth/2+cageOffset.x,screenHeight/2+cageOffset.y)
        )
        drawLine(
            color = Color.Red,
            start = Offset( screenWidth/2 + ropeStart.x,screenHeight/2+ropeStart.y),
            end = Offset( screenWidth/2+ ropeEnd.x,screenHeight/2+ropeEnd.y),
            strokeWidth = 20f
        )
        drawRoundRect(
            color = Color.Yellow,
            size = Size(200f,200f),
            topLeft = Offset( screenWidth/2+animalOffset.x,screenHeight/2+animalOffset.y),
            cornerRadius = CornerRadius(100f,50f)
        )
    }
}

@Preview
@Composable
fun CageMatchPreview(){
    CageMatchScreen({},{},0, CageMatchViewModel())
}