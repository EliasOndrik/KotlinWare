package com.example.kotlinware.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.kotlinware.ui.AppViewModelProvider
import com.example.kotlinware.ui.game.GameSelectionScreen
import com.example.kotlinware.ui.menu.BottomMenuBar
import com.example.kotlinware.ui.menu.MenuDestination
import com.example.kotlinware.ui.menu.TopMenuBar
import com.example.kotlinware.ui.minigames.IntermissionScreen
import com.example.kotlinware.ui.minigames.tapping.BallonPopScreen
import com.example.kotlinware.ui.minigames.tapping.TappingGamesScreen
import com.example.kotlinware.ui.shop.ShopScreen
import com.example.kotlinware.ui.title.TitleScreen

enum class AppDestination{
    MENU,
    GAME
}

@Composable
fun KotlinWareNavHost(
    viewModel: NavigationViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navHostController: NavHostController = rememberNavController()
){
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { TopMenuBar(coins = playerState.money) },
        bottomBar = { BottomMenuBar(
            {navHostController.navigate(MenuDestination.TITLE.name)},
            {navHostController.navigate(MenuDestination.GAMES.name)},
            {navHostController.navigate(MenuDestination.SHOP.name)}
        ) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            NavHost(
                navController = navHostController,
                startDestination = AppDestination.MENU.name
            ){
                navigation(
                    startDestination = MenuDestination.TITLE.name,
                    route = AppDestination.MENU.name
                ){
                    composable(
                        route = MenuDestination.TITLE.name
                    ){
                        TitleScreen()
                    }
                    composable(
                        route = MenuDestination.GAMES.name
                    ){
                        GameSelectionScreen(onPlayClick = {
                            name->
                            navHostController.navigate("tap")

                        })
                    }
                    composable(
                        route = MenuDestination.SHOP.name
                    ){
                        ShopScreen()
                    }
                }
                navigation(
                    startDestination = "tap",
                    route = AppDestination.GAME.name
                ){
                    composable(
                        route = "tap"
                    ){
                        TappingGamesScreen( onQuit = {navHostController.navigate(MenuDestination.GAMES.name)})
                    }
                }
            }
        }

    }

}