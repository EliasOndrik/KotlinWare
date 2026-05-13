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
fun FootballSkillScreen(
    onSuccess:()-> Unit,
    onFail:()->Unit,
    rotations: FloatArray,
    deltaTime:Long,
    viewModel: FootballSkillViewModel
){
    val ballOffset by viewModel.ballOffset.collectAsStateWithLifecycle()
    val leftLeg by viewModel.leftLeg.collectAsStateWithLifecycle()
    val rightLeg by viewModel.rightLeg.collectAsStateWithLifecycle()
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
        drawRect(
            color = Color.Cyan,
            topLeft = Offset(screenWidth/2f-100f,screenHeight/2f-400f),
            size = Size(200f,400f)
        )
        rotate(
            degrees = leftLeg,
            pivot = Offset(screenWidth/2f+50f,screenHeight/2f)
        ){
            drawRect(
                color = Color.Cyan,
                topLeft = Offset(screenWidth/2f+1f,screenHeight/2f),
                size = Size(100f,300f)
            )
        }
        rotate(
            degrees = rightLeg,
            pivot = Offset(screenWidth/2f-50f,screenHeight/2f)
        ){
            drawRect(
                color = Color.Cyan,
                topLeft = Offset(screenWidth/2f-101f,screenHeight/2f),
                size = Size(100f,300f)
            )
        }
        drawCircle(
            color = Color.Gray,
            radius = 100f,
            center = Offset(screenWidth/2f+ ballOffset.x, screenHeight/2f + ballOffset.y)
        )
    }
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text("Kick", fontSize = 40.sp)
        }
    }
}

@Preview
@Composable
fun FootballSkillPreview(){
    FootballSkillScreen({},{}, FloatArray(3), 0,FootballSkillViewModel())
}