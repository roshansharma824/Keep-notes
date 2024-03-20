package com.example.keepnotes.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.keepnotes.R
import com.example.keepnotes.data.local.InMemoryCache
import com.example.keepnotes.ui.theme.DIMENS_0dp
import com.example.keepnotes.ui.theme.DIMENS_32dp
import com.example.keepnotes.ui.theme.DIMENS_8dp
import com.example.keepnotes.ui.theme.GrayTextColor
import com.example.keepnotes.ui.theme.TEXT_SIZE_18sp
import com.example.keepnotes.ui.theme.TopBarBackgroundColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun SelectedTopBar(
    onClickAction: () -> Unit,
    onClickMenu: () -> Unit,
    onDelete:() ->Unit,
    selectItemCount: Int = 1
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(TopBarBackgroundColor)
    }
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.padding(horizontal = DIMENS_0dp, vertical = DIMENS_0dp)
    ) {
        TopAppBar(
            backgroundColor = Color.Transparent,
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_grid),
                        contentDescription = "Grid Icon",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
                DropdownMenuOptions(
                    expanded,
                    onDismiss = { expanded = false },
                    onDelete = {
                        expanded = false
                        onDelete.invoke()
                    })


            },
            elevation = 0.dp,
            title = {
            },
            modifier = Modifier
                .background(
                    color = TopBarBackgroundColor,
                    shape = RectangleShape
                )
                .padding(horizontal = DIMENS_8dp),
            navigationIcon = {
                IconButton(onClick = { onClickAction.invoke() }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Localized description",
                        tint = Color.White
                    )

                }
                IconButton(onClick = { }) {
                    Text(
                        text = "$selectItemCount",
                        style = TextStyle(
                            fontSize = TEXT_SIZE_18sp,
                            lineHeight = 20.sp,

                            fontWeight = FontWeight(400),
                            color = GrayTextColor,
                            textAlign = TextAlign.Left
                        ),
                        textAlign = TextAlign.Left
                    )

                }

            }
        )


    }


}