package com.example.kotlinware.ui.minigames.draging

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DropInBucketScreen(
    onSuccess:()->Unit,
    onFail:()->Unit,
    deltaTime:Long,
    viewModel: DropInBucketViewModel
){
    val waterDrops by viewModel.waterDrops.collectAsStateWithLifecycle()
    val bucketOffset by viewModel.bucketOffset.collectAsStateWithLifecycle()
    Canvas(
        modifier = Modifier.fillMaxSize().pointerInput(Unit){
            detectDragGestures(
                onDrag = {change, dragAmount ->
                    change.consume()
                    viewModel.updateBucket(dragAmount)
                },
            )
        }
    ) {
        viewModel.update(deltaTime)
        if(viewModel.success()){
            onSuccess()
        }
        if (viewModel.fail()){
            onFail()
        }
        val screenWidth = size.width
        val screenHeight = size.height
        drawRect(
            color = Color.LightGray,
            size = Size(viewModel.bucketSize.width,viewModel.bucketSize.height),
            topLeft = Offset(screenWidth/2+bucketOffset.x, screenHeight/2+bucketOffset.y)
        )
        waterDrops.forEach { water->
            if(!water.collected){
                drawCircle(
                    color = Color.Cyan,
                    radius = 50f,
                    center = Offset(screenWidth/2f +water.x, screenHeight/2f+water.y)
                )
            }

        }
    }
}

@Preview
@Composable
fun DropInBucketPreview(){
    DropInBucketScreen({},{}, 0L,DropInBucketViewModel())
}