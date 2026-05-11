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
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CatTapScreen(
    deltaTime: Long,
    onGameSuccess: ()->Unit,
    viewModel: CatTapViewModel
){
    val cats by viewModel.cats.collectAsStateWithLifecycle()
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text("Whack the cat", fontSize = 40.sp)
        }
    }
    Canvas(
        modifier = Modifier.fillMaxSize().pointerInput(Unit){
            detectTapGestures { offset ->
                viewModel.checkCollision(offset - Offset(size.width/2f,size.height/2f))
                if (viewModel.success()){
                    onGameSuccess()
                }
            }

        }
    ){
        viewModel.update(deltaTime)
        val screenWidth = size.width
        val screenHeight = size.height
        cats.forEach { cat ->
            drawRect(
                color = Color.Yellow,
                size = Size(viewModel.width,viewModel.height),
                topLeft = Offset(screenWidth/2f + cat.x,screenHeight/2f + cat.y)
            )
        }
    }
}
@Preview
@Composable
fun CatTapPreview(){
    CatTapScreen(0L,{},CatTapViewModel())
}