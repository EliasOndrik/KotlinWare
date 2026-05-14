package com.example.kotlinware.ui

import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinware.KotlinWareApplication
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kotlinware.ui.game.GameSelectionViewModel
import com.example.kotlinware.ui.minigames.AbstractManagerViewModel
import com.example.kotlinware.ui.minigames.draging.DraggingGamesViewModel
import com.example.kotlinware.ui.minigames.rotating.RotatingGamesScreen
import com.example.kotlinware.ui.minigames.rotating.RotatingGamesViewModel
import com.example.kotlinware.ui.minigames.tapping.TappingGamesViewModel
import com.example.kotlinware.ui.navigation.NavigationViewModel
import com.example.kotlinware.ui.shop.ShopViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ShopViewModel(kotlinWareApplication().container.gameRepository)
        }
        initializer {
            NavigationViewModel(kotlinWareApplication().container.gameRepository)
        }
        initializer {
            GameSelectionViewModel(kotlinWareApplication().container.gameRepository)
        }
        initializer {
            TappingGamesViewModel(kotlinWareApplication().container.gameRepository)
        }
        initializer {
            val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
            RotatingGamesViewModel(application,kotlinWareApplication().container.gameRepository)

        }
        initializer {
            DraggingGamesViewModel(kotlinWareApplication().container.gameRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.kotlinWareApplication(): KotlinWareApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as KotlinWareApplication)
