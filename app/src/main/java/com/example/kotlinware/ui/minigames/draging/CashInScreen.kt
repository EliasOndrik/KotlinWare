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
fun CashInScreen(
    onSuccess:()->Unit,
    viewModel: CashInViewModel
){
    val coins by viewModel.coins.collectAsStateWithLifecycle()
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
                    viewModel.dropCoins()
                }
            )
        }
    ) {
        if (viewModel.success()){
            onSuccess()
        }
        val screenWidth = size.width
        val screenHeight = size.height
        drawRect(
            color = Color.Cyan,
            size = Size(400f, 200f),
            topLeft = Offset(screenWidth/2-200f,screenHeight/2+200f)
        )
        coins.forEach {
            if (!it.collected){
                drawCircle(
                    color = Color.Yellow,
                    radius = 50f,
                    center = Offset(screenWidth/2+it.x,screenHeight/2+ it.y)
                )
            }
        }
    }
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text("Cash in", fontSize = 40.sp)
        }
    }
}

@Preview
@Composable
fun CashInPreview(){
    CashInScreen({},CashInViewModel())
}