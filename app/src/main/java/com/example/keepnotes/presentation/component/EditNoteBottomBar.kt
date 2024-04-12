package com.example.keepnotes.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.TextFormat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.BottomNavigationHeight
import com.example.keepnotes.ui.theme.DIMENS_12dp
import com.example.keepnotes.ui.theme.GrayTextColor
import com.example.keepnotes.ui.theme.TEXT_SIZE_14sp
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun EditNoteBottomBar(
    modifier: Modifier = Modifier,
    updatedAt: Long,
    isShowTextEditorPanel: () -> Unit,

    ) {

    val sdf = SimpleDateFormat("MMM dd")
    val resultdate = Date(updatedAt)
    val date = sdf.format(resultdate)

    Row(
        modifier = Modifier
            .background(color = BackgroundColor, shape = RectangleShape)
            .height(
                BottomNavigationHeight
            )
            .fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically
    ) {

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()) {
            // Create references for the composables to constrain
            val (leftMenu,rightMenu, text) = createRefs()

            IconButton(
                onClick = { isShowTextEditorPanel.invoke() },
                modifier = Modifier.constrainAs(leftMenu) {
                    start.linkTo(parent.start)
                }) {
                Icon(
                    imageVector = Icons.Outlined.TextFormat,
                    contentDescription = "MoreVert",
                    tint = Color.White
                )
            }

            Text(
                text = "Edited $date",
                style = TextStyle(
                    fontSize = TEXT_SIZE_14sp,
                    fontWeight = FontWeight(300),
                    color = GrayTextColor,
                    textAlign = TextAlign.Center

                ),
                modifier = Modifier
                    .padding(bottom = DIMENS_12dp)
                    .constrainAs(text) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
            )

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.constrainAs(rightMenu) {
                    end.linkTo(parent.end)
                }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "MoreVert",
                    tint = Color.White
                )
            }

        }


    }
}

@Preview
@Composable
fun EditNoteBottomBarPreview() {
    EditNoteBottomBar(
        updatedAt = System.currentTimeMillis(),
        isShowTextEditorPanel = {}
    )
}