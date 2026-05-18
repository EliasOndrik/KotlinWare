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
fun TuneFinderScreen(
    onSuccess:()->Unit,
    rotations: FloatArray,
    viewModel: TuneFinderViewModel
){
    val radioColor by viewModel.radioColor.collectAsStateWithLifecycle()
    val radioRotation by viewModel.currentRotationZ.collectAsStateWithLifecycle()
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
            color = Color.Gray,
            topLeft = Offset(screenWidth/2-400f,screenHeight/2-200f),
            size = Size(800f,400f)
        )
        rotate(
            degrees = radioRotation,
            pivot = Offset(screenWidth/2 + 50f,screenHeight/2 + 50f)
        ){
            drawRect(
                color = Color.DarkGray,
                size = Size(100f, 100f),
                topLeft = Offset(screenWidth/2,screenHeight/2)
            )
        }

        drawRect(
            color = radioColor,
            size = Size(300f, 100f),
            topLeft = Offset(screenWidth/2,screenHeight/2-150f)
        )
        drawCircle(
            color = Color.LightGray,
            radius = 150f,
            center = Offset(screenWidth/2-200f,screenHeight/2)
        )
    }
}

@Preview
@Composable
fun TuneFinderPreview(){
    TuneFinderScreen({}, FloatArray(3), TuneFinderViewModel())
}