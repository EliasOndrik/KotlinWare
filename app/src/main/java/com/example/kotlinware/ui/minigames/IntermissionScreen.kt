package com.example.kotlinware.ui.minigames

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            style = TextStyle(fontSize = 100.sp, color = Color.Cyan)
        )
    }
}
@Composable
fun GameOverScreen(
    onQuit:()->Unit,
    onRetry:()->Unit,
    score: Int
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Final Score",
                fontSize = 50.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Text(
                score.toString(),
                fontSize = 40.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {onRetry()},
                    shape = RectangleShape,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Retry")
                }
                Button(
                    onClick = {onQuit()},
                    shape = RectangleShape,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Quit")
                }
            }
        }
    }
}

@Preview
@Composable
fun IntermissionScreenPreview(){
    IntermissionScreen(0,0)
    GameOverScreen({},{},0)
}