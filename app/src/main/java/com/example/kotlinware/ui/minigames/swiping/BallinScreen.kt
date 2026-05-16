package com.example.kotlinware.ui.minigames.swiping

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BallinScreen(
    onSuccess:()->Unit,
    deltaTime:Long,
    viewModel: BallinViewModel
){
    val ballOffset by viewModel.ballOffset.collectAsStateWithLifecycle()
    val basketOffset by viewModel.basketOffset.collectAsStateWithLifecycle()
    val ballRadius by viewModel.ballRadius.collectAsStateWithLifecycle()
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
                    viewModel.drop()
                }
            )
        }
    ) {
        viewModel.update(deltaTime)
        if (viewModel.success()){
            onSuccess()
        }
        val screenWidth = size.width
        val screenHeight = size.height

        drawRect(
            color = Color.DarkGray,
            size = Size(200f,200f),
            topLeft = Offset(screenWidth/2+basketOffset.x,screenHeight/2+basketOffset.y)
        )
        drawOval(
            color = Color.White,
            size = Size(180f,50f),
            topLeft = Offset(screenWidth/2+basketOffset.x +10f,screenHeight/2+basketOffset.y+100f)
        )
        drawCircle(
            color = Color.Red,
            radius = ballRadius,
            center = Offset(screenWidth/2+ballOffset.x,screenHeight/2+ballOffset.y)
        )
    }
}

@Preview
@Composable
fun BallinPreview(){
    BallinScreen({},0L, BallinViewModel())
}