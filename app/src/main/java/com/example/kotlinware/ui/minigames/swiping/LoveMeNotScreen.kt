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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoveMeNotScreen(
    onSuccess:()->Unit,
    deltaTime:Long,
    viewModel: LoveMeNotViewModel
){
    val leafs by viewModel.leafs.collectAsStateWithLifecycle()
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text("Pull", fontSize = 40.sp)
        }
    }
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
        val screenWidth = size.width
        val screenHeight = size.height

        drawLine(
            color = Color.Green,
            start = Offset(screenWidth/2, screenHeight/2),
            end = Offset(screenWidth/2, screenHeight/2+600f),
            strokeWidth = 10f
        )
        leafs.forEach {
            rotate(
                degrees = it.angle
            ){
                drawOval(
                    color = Color.Yellow,
                    size = Size(300f, 100f),
                    topLeft = Offset(screenWidth/2+it.x, screenHeight/2+it.y)
                )
            }
        }
        drawCircle(
            color = Color.Green,
            radius = 80f
        )


    }
}

@Preview
@Composable
fun LoveMeNotPreview(){
    LoveMeNotScreen({},0L, LoveMeNotViewModel())
}