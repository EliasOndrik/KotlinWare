package com.example.kotlinware.ui.minigames

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun IntermissionScreen(
    life:Int = 4,
    score:Int = 0
){
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        for (i in 1..life){
            drawCircle(
                color = Color.Red,
                radius = 100f,
                center = Offset(canvasWidth/5+(i-1)*canvasWidth/5,canvasHeight/4)
            )
        }
        val measuredText = textMeasurer.measure(
            text = score.toString(),
            style = TextStyle(fontSize = 100.sp)

            )
        drawText(
            text = score.toString(),
            textMeasurer = textMeasurer,
            topLeft = Offset((canvasWidth-measuredText.size.width)/2,(3*canvasHeight/2-measuredText.size.height)/2),
            style = TextStyle(fontSize = 100.sp)
        )
    }
}

@Preview
@Composable
fun IntermissionScreenPreview(){
    IntermissionScreen()
}