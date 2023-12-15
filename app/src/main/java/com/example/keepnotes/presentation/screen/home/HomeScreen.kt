package com.example.keepnotes.presentation.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.R

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.BlackBackground
import com.example.keepnotes.ui.theme.*
import com.example.keepnotes.ui.theme.*


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    Scaffold(
        modifier = Modifier
            .padding(top = DIMENS_16dp, start = DIMENS_16dp, end = DIMENS_16dp)
            .background(color = BackgroundColor),
        topBar = {
            TopAppBar(
                backgroundColor = Color.Transparent,
                contentColor = Color.Transparent,
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            painter = painterResource(id = com.example.keepnotes.R.drawable.icon_grid),
                            contentDescription = "Grid Icon",
                            tint = Color.White
                        )
                    }
                    Image(
                        painter = painterResource(com.example.keepnotes.R.drawable.profile_img),
                        contentDescription = "profile img",
                        contentScale = ContentScale.Fit,            // crop the image if it's not a square
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)                       // clip to the circle shape
                            .border(1.dp, Color.Gray, CircleShape)   // add a border (optional)
                    )
                },
                elevation = 0.dp,
                title = {
                    Text(
                        text = "Search yours notes",

                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        color = Color.White,
                        fontWeight = FontWeight.Light,
                        fontSize = TEXT_SIZE_18sp
                    )
                },
                modifier = Modifier.background(
                    color = TopBarBackgroundColor,
                    shape = RoundedCornerShape(50)
                ).padding(end = DIMENS_8dp),
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        backgroundColor = BackgroundColor,
    ) { padding ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .background(color = BackgroundColor)

                .fillMaxWidth()
        ) {

//            Card(
//                border = BorderStroke(width = 1.dp, color = CardBorder),
//                modifier = Modifier,
//                backgroundColor = BackgroundColor
//            ) {
//
//                Text(text = "hello world", modifier = Modifier.padding(DIMENS_16dp))
//
//            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}


