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
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LightCandleScreen(
    onSuccess:()->Unit,
    deltaTime:Long,
    viewModel: LightCandleViewModel
){
    val candles by viewModel.candles.collectAsStateWithLifecycle()
    val lightOffset by viewModel.lightOffset.collectAsStateWithLifecycle()
    Canvas(
        modifier = Modifier.fillMaxSize().pointerInput(Unit){
            detectDragGestures(
                onDragStart = {offset->
                    viewModel.checkCollision(offset - Offset(size.width/2f,size.height/2f))
                },
                onDrag = {change, dragAmount ->
                    change.consume()
                    viewModel.updatePosition(dragAmount)
                },
                onDragEnd = {
                    viewModel.dropIt()
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
            color = Color(0xfff7941d),
            size = Size(800f, 200f),
            topLeft = Offset(screenWidth/2-400f,screenHeight/2)
        )
        scale(
            scaleX = 1f,
            scaleY = 0.5f
        ){
            drawCircle(
                color = Color(0xfff7941d),
                radius = 400f,
                center = Offset(screenWidth/2,screenHeight/2+400f)
            )
            drawCircle(
                color = Color.White,
                radius = 400f
            )
        }
        candles.forEach {
            if (it.lit){
                drawArc(
                    color = Color.Red,
                    startAngle = 50f,
                    sweepAngle = 80f,
                    useCenter = true,
                    size = Size(100f,200f),
                    topLeft = Offset(screenWidth/2f+it.x -25f,screenHeight/2f+ it.y - 200f)
                )
            }
            drawRect(
                color = Color.Magenta,
                size = Size(50f, 200f),
                topLeft = Offset(screenWidth/2 + it.x,screenHeight/2 + it.y)
            )
        }
        translate(
            left = lightOffset.x,
            top = lightOffset.y
        ) {
            drawLine(
                color = Color.Yellow,
                start = Offset(screenWidth/2f,screenHeight/2f+100f),
                end = Offset(screenWidth/2+200f,screenHeight/2),
                strokeWidth = 20f
            )
            drawArc(
                color = Color.Red,
                startAngle = 50f,
                sweepAngle = 80f,
                useCenter = true,
                size = Size(100f,200f),
                topLeft = Offset(screenWidth/2f-50f,screenHeight/2f-100f)
            )
        }

    }
}

@Preview
@Composable
fun LightCandlePreview(){
    LightCandleScreen({},0, LightCandleViewModel())
}