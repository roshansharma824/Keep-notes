package com.example.keepnotes.presentation.component

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
import com.example.keepnotes.R
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.DIMENS_16dp
import com.example.keepnotes.ui.theme.DIMENS_20dp
import com.example.keepnotes.ui.theme.DIMENS_24dp
import com.example.keepnotes.ui.theme.DIMENS_8dp
import com.example.keepnotes.ui.theme.TEXT_SIZE_16sp
import com.example.keepnotes.ui.theme.TEXT_SIZE_24sp
import kotlinx.coroutines.launch


@Composable
fun DrawerAppComponent() {
    // Reacting to state changes is the core behavior of Compose
    // @remember helps to calculate the value passed to it only
    // during the first composition. It then
    // returns the same value for every subsequent composition.
    // @mutableStateOf as an observable value where updates to
    // this variable will redraw all
    // the composable functions. "only the composable
    // that depend on this will be redraw while the
    // rest remain unchanged making it more efficient".
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    // State composable used to hold the
    // value of the current active screen
    val currentScreen = remember { mutableStateOf(DrawerAppScreen.Notes) }

    val coroutineScope = rememberCoroutineScope()

    ModalDrawer(
        drawerShape = RoundedCornerShape(topEnd = DIMENS_20dp, bottomEnd = DIMENS_20dp),
        // Drawer state indicates whether
        // the drawer is open or closed.
        drawerState = drawerState, gesturesEnabled = drawerState.isOpen, drawerContent = {
            //drawerContent accepts a composable to represent
            // the view/layout that will be displayed
            // when the drawer is open.
            DrawerContentComponent(
                // We pass a state composable that represents the
                // current screen that's selected
                // and what action to take when the drawer is closed.
                currentScreen = currentScreen,
                closeDrawer = { coroutineScope.launch { drawerState.close() } })
        }, content = {
            // bodyContent takes a composable to
            // represent the view/layout to display on the
            // screen. We select the appropriate screen
            // based on the value stored in currentScreen.
            BodyContentComponent(currentScreen = currentScreen.value, openDrawer = {
                coroutineScope.launch { drawerState.open() }
            })
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
            // Box with clickable modifier wraps the
            // child composable and enables it to react to a
            // click through the onClick callback similar
            // to the onClick listener that we are
            // accustomed to on Android.
            // Here, we just update the currentScreen variable
            // to hold the appropriate value based on
            // the row that is clicked i.e if the first
            // row is clicked, we set the value of
            // currentScreen to DrawerAppScreen.Screen1,
            // when second row is clicked we set it to
            // DrawerAppScreen.Screen2 and so on and so forth.
            val screen = getScreenBasedOnIndex(index)
            Column(Modifier.clickable(onClick = {
                currentScreen.value = screen
                // We also close the drawer when an
                // option from the drawer is selected.
                closeDrawer()
            }), content = {
                // bodyContent takes a composable to
                // represent the view/layout to display on the
                // screen.
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DIMENS_8dp)
                        .clip(shape = RoundedCornerShape(50)),
                    // We set the color of the row based on whether
                    // that row represents the current
                    // screen that's selected. We only want to
                    // highlight the row that's selected.
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

@Composable
fun BodyContentComponent(
    currentScreen: DrawerAppScreen, openDrawer: () -> Unit
) {
    when (currentScreen) {
        DrawerAppScreen.Notes -> Screen1Component(
            openDrawer
        )

        DrawerAppScreen.Reminders -> Screen2Component(
            openDrawer
        )

        DrawerAppScreen.CreateNewLable -> Screen3Component(
            openDrawer
        )
        DrawerAppScreen.Archive -> Screen1Component(
            openDrawer
        )
        DrawerAppScreen.Deleted -> Screen1Component(
            openDrawer
        )
        DrawerAppScreen.Settings -> Screen1Component(
            openDrawer
        )
        DrawerAppScreen.HelpFeedback -> Screen1Component(
            openDrawer
        )

        else -> {
            Screen1Component(
                openDrawer
            )
        }
    }
}

@Composable
fun Screen1Component(openDrawer: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // TopAppBar has slots for a title, navigation icon,
        // and actions. Also known as the action bar.
        HomeScreenTopBar(onClickAction = openDrawer)
        Surface(color = BackgroundColor, modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(text = "Keep Notes 1")
                })
        }
    }
}

@Composable
fun Screen2Component(openDrawer: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        HomeScreenTopBar(onClickAction = openDrawer)
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
        HomeScreenTopBar(onClickAction = openDrawer)
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
    DrawerAppComponent()
}
