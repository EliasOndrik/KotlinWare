package com.example.kotlinware.ui.title


import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TitleScreen(){
    // Získame informáciu o aktuálnej konfigurácii zariadenia
    val configuration = LocalConfiguration.current

    // Zistíme, či je mobil otočený horizontálne (Landscape)
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ){
        Text("Logo")
    }

}
@Preview
@Composable
fun TitleScreenPreview(){
    TitleScreen()
}