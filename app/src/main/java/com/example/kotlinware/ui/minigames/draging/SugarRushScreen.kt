package com.example.kotlinware.ui.minigames.draging

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
fun SugarRushScreen(
    onSuccess:()->Unit,
    deltaTime:Long,
    viewModel: SugarRushViewModel
){
    val candies by viewModel.candies.collectAsStateWithLifecycle()
    val tongueEndOffset by viewModel.tongueEnd.collectAsStateWithLifecycle()
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
                    viewModel.dropIt()
                    if (viewModel.success()){
                        onSuccess()
                        viewModel.done()
                    }
                }
            )
        }
    ) {
        viewModel.update(deltaTime)
        val screenWidth = size.width
        val screenHeight = size.height
        if(!viewModel.isDone()){
            drawLine(
                color = Color.Red,
                start = Offset(screenWidth/2-400f,screenHeight/2+200f),
                end = Offset(screenWidth/2+tongueEndOffset.x,screenHeight/2+tongueEndOffset.y),
                strokeWidth = 20f
            )
        }
        drawArc(
            color = Color.LightGray,
            startAngle = 0f,
            sweepAngle = 300f,
            useCenter = true,
            size = Size(200f,200f),
            topLeft = Offset(screenWidth/2-500f,screenHeight/2+100f)
        )
        if(viewModel.isDone()){
            drawCircle(
                color = Color.LightGray,
                radius = 100f,
                center = Offset(screenWidth/2-400f,screenHeight/2+200f)
            )
        } else {
            candies.forEach {
                drawCircle(
                    color = Color.Green,
                    radius = 50f,
                    center = Offset(screenWidth/2+it.x,screenHeight/2+it.y)
                )
            }
        }
    }
}


@Preview
@Composable
fun SugarRushPreview(){
    SugarRushScreen({},0, SugarRushViewModel())
}