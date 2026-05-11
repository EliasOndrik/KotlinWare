package com.example.kotlinware.ui.minigames.tapping


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
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
fun CorrectOrderScreen(
    onGameSuccess: ()-> Unit,
    onGameFail:()-> Unit,
    viewModel: CorrectOrderViewModel
){
    val blocks by viewModel.blocks.collectAsStateWithLifecycle()
    Canvas(
        modifier = Modifier.fillMaxSize().pointerInput(Unit){
            detectTapGestures { offset ->
                viewModel.checkCollision(offset - Offset(size.width/2f,size.height/2f))
                if (viewModel.success()){
                    onGameSuccess()
                }
                if (viewModel.fail()){
                    onGameFail()
                }
            }
        }
    ){
        val screenWidth = size.width
        val screenHeight = size.height
        blocks.asReversed().forEach { block->
            if (block.visible){
                drawRect(
                    block.color,
                    size = Size(viewModel.size,viewModel.size),
                    topLeft = Offset(screenWidth/2f + block.x, screenHeight/2f + block.y)
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
            Text("Red Green Blue", fontSize = 40.sp)
        }
    }
}

@Preview
@Composable
fun CorrectOrderPreview(){
    CorrectOrderScreen({}, {},CorrectOrderViewModel())
}