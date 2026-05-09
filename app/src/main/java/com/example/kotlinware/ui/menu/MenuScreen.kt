package com.example.kotlinware.ui.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinware.R
import com.example.kotlinware.ui.theme.KotlinWareTheme

@Composable
fun TopMenuBar (
    coins:Int = 0,
    padding:Int = 0
) {
    Surface(
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().size(50.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row (
                modifier = Modifier
                    .padding(10.dp)
                    .border(2.dp, MaterialTheme.colorScheme.tertiary,shape = CircleShape)
                    .size(150.dp)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text( text = "Coins: ")
                Spacer(modifier = Modifier.weight(0.9f))
                Text(text = coins.toString())
            }
        }
    }

}
@Composable
fun BottomMenuBar (
    onTitleButtonClick: () -> Unit,
    onGamesButtonClick: () -> Unit,
    onShopButtonClick: () -> Unit,
    padding: Int = 0,
) {
    Row(
        modifier = Modifier.fillMaxWidth().size(50.dp).background(MaterialTheme.colorScheme.secondary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onTitleButtonClick,
            shape = RectangleShape,
            modifier = Modifier.fillMaxHeight(0.9f).size(100.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)
        ) {
            Text(stringResource(R.string.title))
        }
        Button(
            onClick = onGamesButtonClick,
            shape = RectangleShape,
            modifier = Modifier.fillMaxHeight(0.9f).size(100.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)
            ) {
            Text(stringResource(R.string.games))
        }
        Button(
            onClick = onShopButtonClick,
            shape = RectangleShape,
            modifier = Modifier.fillMaxHeight(0.9f).size(100.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)
            ) {
            Text(stringResource(R.string.shop))
        }
    }
}

enum class MenuDestination(
    val id:Int
){
    TITLE(R.string.title),
    GAMES(R.string.games),
    SHOP(R.string.shop)
}

@Preview
@Composable
fun MenuPreview(){
    KotlinWareTheme() {
        Scaffold(
            topBar = {TopMenuBar()},
            bottomBar = {BottomMenuBar({},{},{})}

        ){ innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) { }
        }
    }


}