package com.example.kotlinware.ui

import com.example.kotlinware.KotlinWareApplication
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kotlinware.ui.game.GameSelectionViewModel
import com.example.kotlinware.ui.minigames.AbstractManagerViewModel
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

    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.kotlinWareApplication(): KotlinWareApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as KotlinWareApplication)
