package com.example.kotlinware.ui.minigames.tapping


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BallonPopScreen(
    onGameSuccess: ()-> Unit ,
    viewModel: BallonPopViewModel
){
    val ballons by viewModel.ballons.collectAsState()
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text("Pop", fontSize = 40.sp)
        }
    }

    Canvas(
        modifier = Modifier.fillMaxSize().pointerInput(Unit){
            detectTapGestures { offset ->
                viewModel.checkBallonsCollision(offset - Offset(size.width/2f,size.height/2f))
                if (viewModel.areBallonsPopped()){
                    onGameSuccess()
                }
            }
        }
    ) {
        val screenWidth = size.width
        val screenHeight = size.height
        scale(
            scaleX = 0.8f,
            scaleY = 1f
        ){
            ballons.asReversed().forEach { ballon ->
                if (!ballon.popped){
                    drawCircle(
                        color = ballon.color,
                        radius = 200f,
                        center = Offset(screenWidth/2 + ballon.x,
                            screenHeight/2 + ballon.y)
                    )
                }

            }


        }


    }
}

@Preview
@Composable
fun BallonPopPreview(){
    BallonPopScreen({}, BallonPopViewModel())
}