package com.example.kotlinware.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinware.R
import com.example.kotlinware.data.Minigame
import com.example.kotlinware.ui.AppViewModelProvider
import com.example.kotlinware.ui.navigation.NavigationViewModel
import kotlinx.coroutines.launch

@Composable
fun GameSelectionScreen(
    viewModel: GameSelectionViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onPlayClick: (String) -> Unit
){
    val minigames by viewModel.playableGameState.collectAsStateWithLifecycle()

    GamePager(
        minigames,
        onPlayClick
    )
}

@Composable
fun GamePager(
    minigames: List<Minigame>,
    onPlayClick:(String)-> Unit = {}
){
    Column(
        modifier = Modifier.background(Color.White)
    ){
        val pagerState = rememberPagerState(pageCount = {
            minigames.count()
        }
        )
        val coroutineScope = rememberCoroutineScope()
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            GamePage(
                name = minigames[page].name,
                score = minigames[page].score
            )
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                        .clickable{
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page = iteration)
                            }
                        }
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Button(
                onClick = {onPlayClick(minigames[pagerState.currentPage].name)},
                shape = RectangleShape
            ) {
                Text("Play")
            }
        }
    }

}

@Composable
fun GamePage(
    name:String = "",
    score:Int = 0
){
    val context = LocalContext.current
    val stringId = remember(name) {
        context.resources.getIdentifier(
            name,
            "string",
            context.packageName
        )
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.background(Color.Cyan).padding(10.dp)
        ){

            Text(stringResource(stringId))
        }
        Box(

        ){
            Image(
                painter = painterResource(R.drawable.kotlingame),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(0.7f).border(2.dp,Color.Cyan)
            )
        }
        Box(
            modifier = Modifier.background(Color.Cyan).padding(10.dp)
        ){
            Text("Score: $score")
        }
    }
}

@Preview
@Composable
fun GameSelectionPreview(){
    GamePager(
        listOf(Minigame(0,"tapping",0,false),
            Minigame(0,"tapping",0,true),
            Minigame(0,"tapping",0,false),
            Minigame(0,"tapping",0,false))
    )
}