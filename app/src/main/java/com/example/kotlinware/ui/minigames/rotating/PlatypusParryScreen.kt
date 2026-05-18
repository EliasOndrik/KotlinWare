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
fun PlatypusParryScreen(
    onSuccess:()-> Unit,
    onFail:()->Unit,
    rotations: FloatArray,
    deltaTime:Long,
    viewModel: PlatypusParryViewModel
){
    val bullets by viewModel.bullets.collectAsStateWithLifecycle()
    val rotation by viewModel.rotation.collectAsStateWithLifecycle()
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        viewModel.update(rotations)
        viewModel.update(deltaTime)
        if (viewModel.success()){
            onSuccess()
        }
        if (viewModel.fail()){
            onFail()
        }
        val screenWidth = size.width
        val screenHeight = size.height
        rotate(
            degrees = rotation-90f,
        ){
            drawCircle(
                color = Color.Cyan,
                radius = 100f
            )
            drawRect(
                color = Color.Yellow,
                size = Size(100f,150f),
                topLeft = Offset(screenWidth/2f-50f,screenHeight/2f+100f)
            )
        }

        bullets.forEach { bullet ->
            if (bullet.active){
                drawCircle(
                    color = Color.Magenta,
                    radius = 50f,
                    center = Offset(screenWidth/2f + bullet.x * bullet.distance,screenHeight/2f + bullet.y * bullet.distance)
                )
            }

        }
    }
}

@Preview
@Composable
fun PlatypusParryPreview(){
    PlatypusParryScreen({},{}, FloatArray(3),0, PlatypusParryViewModel())
}