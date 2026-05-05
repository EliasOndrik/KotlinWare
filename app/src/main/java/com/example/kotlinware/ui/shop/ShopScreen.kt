package com.example.kotlinware.ui.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinware.R
import com.example.kotlinware.data.Minigame
import com.example.kotlinware.ui.AppViewModelProvider

@Composable
fun ShopScreen(
    viewModel: ShopViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val minigames by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.background(Color.White).padding(20.dp),
    ) {
        Shelf(minigames)
    }
}

@Composable
fun Shelf(
    minigameList: List<Minigame>
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(
            items = minigameList,

        ){ minigame ->
            PurchasableGame(
                isPurchased = minigame.unlocked,
                onGameClick = {},
                name = minigame.name
            )
        }
    }


}

@Composable
fun PurchasableGame(
    name: String = "",
    isPurchased: Boolean = false,
    onGameClick: ()->Unit,
    price: Int = 40,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
            .padding(10.dp)
            .background(Color.Blue)
            .clickable{},
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(!isPurchased){
                Box(
                    modifier = Modifier.fillMaxHeight(0.8f)
                ){
                    Image(
                        painter = painterResource(R.drawable.kotlingame),
                        contentDescription = ""
                    )
                }
            }
            Box(
                modifier = Modifier.background(Color.White).padding(10.dp)
            ){
                if (isPurchased){
                    Text("Sold")
                } else {
                    Text("Pay $price coins")
                }
            }
        }
    }
}

@Preview
@Composable
fun ShopPreview(){
    Shelf(
        listOf(Minigame(0,"shit",0,false),
            Minigame(0,"shit",0,true),
            Minigame(0,"shit",0,false),
            Minigame(0,"shit",0,false))
    )
}