package com.example.keepnotes.navigation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigation
import androidx.navigation.compose.rememberNavController
import com.example.keepnotes.R
import com.example.keepnotes.presentation.screen.home.HomeScreen
import com.example.keepnotes.ui.theme.DIMENS_64dp
import com.example.keepnotes.ui.theme.TEXT_SIZE_18sp
import kotlinx.coroutines.launch


@Composable
fun NavigationDrawer() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope= rememberCoroutineScope()

    val items = listOf(
        DrawerItem(icon = Icons.Default.Favorite, label = "Likes", secondaryLabel = "34"),
        DrawerItem(icon = Icons.Default.Face, label = "Messages", secondaryLabel = "34"),
        DrawerItem(icon = Icons.Default.Email, label = "Mails", secondaryLabel = "34"),
        DrawerItem(icon = Icons.Default.Settings, label = "Settings", secondaryLabel = ""),
    )

    var selectedItem by remember {
        mutableStateOf(items[0])
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = DIMENS_64dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Header", fontSize = TEXT_SIZE_18sp)
                }
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.label) },
                        selected = item == selectedItem,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            selectedItem = item
                        })
                }
            }
        },
        content = {
            Column (
                modifier = Modifier.clickable {
                    scope.launch {
                        drawerState.open()
                    }
                }
            ) {

            }
        }

    )

}

data class DrawerItem(
    val icon: ImageVector,
    val label: String,
    val secondaryLabel: String
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    NavigationDrawer()
}






















