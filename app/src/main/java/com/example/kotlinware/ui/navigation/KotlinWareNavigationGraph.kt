package com.example.kotlinware.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.kotlinware.ui.game.GameSelectionScreen
import com.example.kotlinware.ui.menu.BottomMenuBar
import com.example.kotlinware.ui.menu.MenuDestination
import com.example.kotlinware.ui.menu.TopMenuBar
import com.example.kotlinware.ui.shop.ShopScreen
import com.example.kotlinware.ui.title.TitleScreen

enum class AppDestination{
    MENU,
    GAME
}

@Composable
fun KotlinWareNavHost(
    viewModel: NavigationViewModel = viewModel(),
    navHostController: NavHostController = rememberNavController()
){
    Scaffold(
        topBar = { TopMenuBar() },
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
                        GameSelectionScreen()
                    }
                    composable(
                        route = MenuDestination.SHOP.name
                    ){
                        ShopScreen()
                    }
                }
            }
        }

    }

}