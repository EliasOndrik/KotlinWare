package com.example.kotlinware

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.kotlinware.ui.game.GameSelectionPreview
import com.example.kotlinware.ui.menu.MenuPreview
import com.example.kotlinware.ui.navigation.KotlinWareNavHost
import com.example.kotlinware.ui.theme.KotlinWareTheme
import com.example.kotlinware.ui.title.TitleScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinWareTheme {
                val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
                windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())

                // Nastaví, aby sa objavil len pri potiahnutí prstom
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                KotlinWareNavHost()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinWareTheme {
        Greeting("Android")
    }
}