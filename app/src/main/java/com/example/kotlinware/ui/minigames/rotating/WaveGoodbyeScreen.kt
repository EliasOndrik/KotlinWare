package com.example.kotlinware.ui.minigames.rotating

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WaveGoodbyeScreen(
    onSuccess:()->Unit,
    rotations: FloatArray,
    viewModel: WaveGoodbyeViewModel
) {
    val rotation by viewModel.currentRotationZ.collectAsStateWithLifecycle()
    val leavingSize by viewModel.leavingSize.collectAsStateWithLifecycle()
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        viewModel.update(rotations)
        if (viewModel.success()){
            onSuccess()
        }
        val screenWidth = size.width
        val screenHeight = size.height

        drawRect(
            color = Color.Yellow,
            size = leavingSize,
            topLeft = Offset((screenWidth-leavingSize.width)/2f, screenHeight/2f-leavingSize.height)
        )
        rotate(
            degrees = rotation,
            pivot = Offset(screenWidth/2f, screenHeight/2f+500f)
        ){
            drawRect(
                color = Color.Red,
                size = Size(200f,500f),
                topLeft = Offset(screenWidth/2f-100f, screenHeight/2f)
            )
        }

    }
}

@Preview
@Composable
fun WaveGoodbyePreview(){
    WaveGoodbyeScreen({},FloatArray(3), WaveGoodbyeViewModel())
}