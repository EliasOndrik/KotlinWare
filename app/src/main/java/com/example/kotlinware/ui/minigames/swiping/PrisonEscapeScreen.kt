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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PrisonEscapeScreen(
    onSuccess:()->Unit,
    viewModel: PrisonEscapeViewModel
){
    val breakPoints by viewModel.breakPoints.collectAsStateWithLifecycle()

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
            )
        }
    ) {
        if (viewModel.success()){
            onSuccess()
        }
        val screenWidth = size.width
        val screenHeight = size.height
        drawRect(
            color = Color.Gray,
            size = Size(700f,700f),
            topLeft = Offset(screenWidth/2-350f,screenHeight/2-350f)
        )
        drawRect(
            color = Color.Blue,
            size = Size(600f,600f),
            topLeft = Offset(screenWidth/2-300f,screenHeight/2-300f)
        )
        if (!viewModel.success()){
            drawLine(
                color = Color.Gray,
                start = Offset(screenWidth/2-100f,screenHeight/2-250f),
                end = Offset(screenWidth/2-100f,screenHeight/2+250f),
                strokeWidth = 50f
            )
            drawLine(
                color = Color.Gray,
                start = Offset(screenWidth/2+100f,screenHeight/2-250f),
                end = Offset(screenWidth/2+100f,screenHeight/2+250f),
                strokeWidth = 50f
            )
            drawLine(
                color = Color.Gray,
                start = Offset(screenWidth/2-250f,screenHeight/2+100f),
                end = Offset(screenWidth/2+250f,screenHeight/2+100f),
                strokeWidth = 50f
            )
            drawLine(
                color = Color.Gray,
                start = Offset(screenWidth/2-250f,screenHeight/2-100f),
                end = Offset(screenWidth/2+250f,screenHeight/2-100f),
                strokeWidth = 50f
            )
        }
        breakPoints.forEach {
            if (!it.broken){
                drawRect(
                    color = Color.Red,
                    size = Size(it.width,it.height),
                    topLeft = Offset(screenWidth/2+it.x,screenHeight/2+it.y)
                )
            }
        }
    }
}

@Preview
@Composable
fun PrisonEscapePreview(){
    PrisonEscapeScreen({}, PrisonEscapeViewModel())
}