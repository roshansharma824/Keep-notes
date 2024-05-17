package com.example.keepnotes.presentation.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.keepnotes.R
import com.example.keepnotes.presentation.screen.allnotes.AllNotesScreen
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.DIMENS_16dp
import com.example.keepnotes.ui.theme.DIMENS_20dp
import com.example.keepnotes.ui.theme.DIMENS_24dp
import com.example.keepnotes.ui.theme.DIMENS_8dp
import com.example.keepnotes.ui.theme.TEXT_SIZE_16sp
import com.example.keepnotes.ui.theme.TEXT_SIZE_24sp
import kotlinx.coroutines.launch


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DrawerAppComponent(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val currentScreen = remember { mutableStateOf(DrawerAppScreen.Notes) }

    val coroutineScope = rememberCoroutineScope()

    ModalDrawer(
        drawerShape = RoundedCornerShape(topEnd = DIMENS_20dp, bottomEnd = DIMENS_20dp),

        drawerState = drawerState, gesturesEnabled = drawerState.isOpen, drawerContent = {

            DrawerContentComponent(
                currentScreen = currentScreen,
                closeDrawer = { coroutineScope.launch { drawerState.close() } })
        }, content = {

            BodyContentComponent(currentScreen = currentScreen.value, openDrawer = {
                coroutineScope.launch { drawerState.open() }
            }, navController = navController, sharedTransitionScope, animatedContentScope)
        })
}

@Composable
fun DrawerContentComponent(
    currentScreen: MutableState<DrawerAppScreen>, closeDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = BackgroundColor,
                shape = RoundedCornerShape(topEnd = DIMENS_20dp, bottomEnd = DIMENS_20dp)
            )
    ) {

        Text(
            text = "Google Keep",
            fontSize = TEXT_SIZE_24sp,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(
                start = DIMENS_16dp,
                top = DIMENS_24dp,
                bottom = DIMENS_16dp
            )
        )

        for (index in DrawerAppScreen.values().indices) {

            val screen = getScreenBasedOnIndex(index)
            Column(Modifier.clickable(onClick = {
                currentScreen.value = screen

                closeDrawer()
            }), content = {

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DIMENS_8dp)
                        .clip(shape = RoundedCornerShape(50)),

                    color = if (currentScreen.value == screen) {
                        MaterialTheme.colors.secondary
                    } else {
                        BackgroundColor
                    }
                ) {
                    Row {
                        Image(
                            painter = when (screen.name) {
                                DrawerAppScreen.Notes.toString() -> {
                                    painterResource(R.drawable.icon_bulb)
                                }

                                DrawerAppScreen.Reminders.toString() -> {
                                    painterResource(R.drawable.icon_bell)
                                }

                                DrawerAppScreen.CreateNewLable.toString() -> {
                                    painterResource(R.drawable.icon_plus)
                                }

                                DrawerAppScreen.Archive.toString() -> {
                                    painterResource(R.drawable.outline_archive_24)
                                }

                                DrawerAppScreen.Deleted.toString() -> {
                                    painterResource(R.drawable.icon_delete)
                                }

                                DrawerAppScreen.Settings.toString() -> {
                                    painterResource(R.drawable.icon_setting)
                                }

                                DrawerAppScreen.HelpFeedback.toString() -> {
                                    painterResource(R.drawable.icon_help_feedback)
                                }

                                else -> {
                                    painterResource(R.drawable.icon_bulb)
                                }
                            },
                            contentDescription = "icons_bulb",
                            contentScale = ContentScale.Fit,            // crop the image if it's not a square
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = DIMENS_16dp)
                                .size(DIMENS_24dp)

                        )
                        Text(
                            text = screen.name,
                            fontWeight = FontWeight(300),
                            modifier = Modifier.padding(16.dp),
                            fontSize = TEXT_SIZE_16sp
                        )
                    }

                }
            })
        }
    }
}

fun getScreenBasedOnIndex(index: Int) = when (index) {
    0 -> DrawerAppScreen.Notes
    1 -> DrawerAppScreen.Reminders
    2 -> DrawerAppScreen.CreateNewLable
    3 -> DrawerAppScreen.Archive
    4 -> DrawerAppScreen.Deleted
    5 -> DrawerAppScreen.Settings
    6 -> DrawerAppScreen.HelpFeedback
    else -> DrawerAppScreen.Notes
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BodyContentComponent(
    currentScreen: DrawerAppScreen,
    openDrawer: () -> Unit,
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    when (currentScreen) {
        DrawerAppScreen.Notes -> sharedTransitionScope.AllNotesScreen(
            navController = navController,
            openDrawer = openDrawer,
            animatedContentScope = animatedContentScope
        )

        DrawerAppScreen.Reminders -> Screen2Component(
            openDrawer
        )

        DrawerAppScreen.CreateNewLable -> Screen3Component(
            openDrawer
        )

        DrawerAppScreen.Archive -> sharedTransitionScope.AllNotesScreen(
            navController = navController,
            openDrawer = openDrawer,
            animatedContentScope = animatedContentScope
        )

        DrawerAppScreen.Deleted -> sharedTransitionScope.AllNotesScreen(
            navController = navController,
            openDrawer = openDrawer,
            animatedContentScope = animatedContentScope
        )

        DrawerAppScreen.Settings -> sharedTransitionScope.AllNotesScreen(
            navController = navController,
            openDrawer = openDrawer,
            animatedContentScope = animatedContentScope
        )

        DrawerAppScreen.HelpFeedback -> sharedTransitionScope.AllNotesScreen(
            navController = navController,
            openDrawer = openDrawer,
            animatedContentScope = animatedContentScope
        )
        else -> {
            sharedTransitionScope.AllNotesScreen(
                navController = navController,
                openDrawer = openDrawer,
                animatedContentScope = animatedContentScope
            )
        }
    }
}



@Composable
fun Screen2Component(openDrawer: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        HomeScreenTopBar(onClickAction = openDrawer, onSearch = {}, onChangeView = {})
        Surface(color = BackgroundColor, modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(text = "Keep Notes 2")
                })
        }
    }
}

@Composable
fun Screen3Component(openDrawer: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        HomeScreenTopBar(onClickAction = openDrawer, onSearch = {}, onChangeView = {})
        Surface(color = BackgroundColor, modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(text = "Keep Notes 3")
                })
        }
    }
}

enum class DrawerAppScreen {
    Notes, Reminders, CreateNewLable, Archive, Deleted, Settings, HelpFeedback
}

@Preview
@Composable
fun DrawerAppComponentPreview() {
//    DrawerAppComponent(
//        navController = rememberNavController(),
//        sharedTransitionScope = sharedTransitionScope
//    )
}
