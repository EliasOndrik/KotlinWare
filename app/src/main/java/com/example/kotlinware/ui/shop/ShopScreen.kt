package com.example.kotlinware.ui.shop

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinware.R
import com.example.kotlinware.data.Minigame
import com.example.kotlinware.ui.AppViewModelProvider
import com.example.kotlinware.ui.theme.KotlinWareTheme

@Composable
fun ShopScreen(
    viewModel: ShopViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val minigames by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.padding(20.dp)

    ) {
        Shelf(minigameList = minigames, onGameClick = {viewModel.onGameClick(it)})
    }

}

@Composable
fun Shelf(
    minigameList: List<Minigame>,
    onGameClick: (Minigame)->Unit,
){
    val context = LocalContext.current
    val imageBitmap = ImageBitmap.imageResource(R.drawable.shelf)

    val gridState = rememberLazyGridState()
    val totalScrollOffset by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) {
                0f
            } else {
                visibleItems.first().offset.y
            }
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier.drawBehind{
            scale(scaleX = size.width/imageBitmap.width, scaleY = 1f){
                translate(left = 0f, top = totalScrollOffset.toFloat()) {
                    for (i in 0..<minigameList.count()/2){
                        drawImage(imageBitmap,
                          topLeft = Offset(((size.width-imageBitmap.width)/2),(i*200+180).dp.toPx())
                        )
                    }
                }
            }
        }

    ) {
        items(
            items = minigameList,

        ){ minigame ->
            PurchasableGame(
                onGameClick = onGameClick,
                minigame = minigame,
                context = context
            )
        }
    }
}

@Composable
fun PurchasableGame(
    minigame: Minigame = Minigame(0,"",0,false),
    onGameClick: (Minigame)->Unit,
    price: Int = 40,
    context: Context = LocalContext.current
){

    val imageId = remember(minigame.name) {
        context.resources.getIdentifier(
            minigame.name,
            "drawable",
            context.packageName
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
            .padding(10.dp)
            .clickable{onGameClick(minigame)},
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if(!minigame.unlocked){
                Box(
                    modifier = Modifier.fillMaxHeight(0.7f)
                ){
                    Image(
                        painter = painterResource(imageId),
                        contentDescription = ""
                    )
                }
            }
            Box(
                modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).padding(10.dp)
            ){
                if (minigame.unlocked){
                    Text("Sold",
                        color = Color.Black)
                } else {
                    Text("Pay $price coins",color = Color.Black)
                }
            }
        }
    }
}

@Preview
@Composable
fun ShopPreview(){
    KotlinWareTheme {
        Shelf(
            listOf(Minigame(0,"tapping",0,false),
                Minigame(0,"tapping",0,true),
                Minigame(0,"rotating",0,false),
                Minigame(0,"tapping",0,false))
        ) {}
    }
}