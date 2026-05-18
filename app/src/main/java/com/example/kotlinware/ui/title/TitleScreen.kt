package com.example.kotlinware.ui.title


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinware.R

@Composable
fun TitleScreen(){

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier.fillMaxWidth(0.8f).size(200.dp)
        ){
            Image(
                painter = painterResource(R.drawable.kotlinwarelogo3),
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}
@Preview
@Composable
fun TitleScreenPreview(){
    TitleScreen()
}