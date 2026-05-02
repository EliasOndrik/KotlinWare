package com.example.kotlinware.ui.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinware.R

@Composable
fun ShopScreen(
    viewModel: ShopViewModel = viewModel()
){
    Column(
        modifier = Modifier.background(Color.White).padding(20.dp),
    ) {
        Shelf()
        Shelf()
        Shelf()
    }
}

@Composable
fun Shelf(

){
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.Cyan),
    ) {
        PurchasableGame(0.5f)
        PurchasableGame(1f)
    }
}

@Composable
fun PurchasableGame(
    fraction: Float,
    isPurchased: Boolean = false,
    price: Int = 40,
){
    Box(
        modifier = Modifier.fillMaxWidth(fraction).size(200.dp).padding(10.dp).background(Color.Blue),
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
    ShopScreen()
}