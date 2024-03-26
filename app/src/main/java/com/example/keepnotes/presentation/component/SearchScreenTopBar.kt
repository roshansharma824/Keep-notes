package com.example.keepnotes.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.keepnotes.ui.theme.*

@Composable
fun SearchScreenTopBar(
    navController: NavController,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () ->Unit
    ) {
    var searchInput by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.padding(horizontal = DIMENS_16dp, vertical = DIMENS_8dp)
    ) {
        TopAppBar(
            backgroundColor = Color.Transparent,
            contentColor = Color.Transparent,
            actions = {
                IconButton(onClick = {
                    searchInput = ""
                    onClearQuery.invoke()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White,
                        modifier = Modifier.size(
                            DIMENS_20dp
                        )
                    )
                }

            },
            elevation = 0.dp,
            title = {
                SearchTextField(text = searchInput, placeholderText = "Search yours notes") { newText ->
                    searchInput = newText
                    onQueryChanged(searchInput)
//                    editNoteViewModel.updateNote(newText)
                }
            },
            modifier = Modifier
                .background(
                    color = TopBarBackgroundColor,
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = DIMENS_0dp),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack()  }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
            }
        )
    }
}

@Composable
fun SearchTextField(
    text: String,
    placeholderText: String,
    onTextChanged: (String) -> Unit
) {
    var isKeyboardVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    TextField(
        value = text,
        onValueChange = {
            onTextChanged(it)
        },
        placeholder = {
            androidx.compose.material3.Text(
                text = placeholderText,
                style = MaterialTheme.typography.titleMedium.copy(color = GrayTextColor.copy(alpha = 0.5f), fontSize = TEXT_SIZE_18sp, fontWeight = FontWeight.W300),
            )
        },
        textStyle = MaterialTheme.typography.titleMedium.copy(color = GrayTextColor.copy(alpha = 0.5f), fontSize = TEXT_SIZE_18sp, fontWeight = FontWeight.W300),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { isKeyboardVisible = true },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = TopBarBackgroundColor,
            focusedBorderColor = BackgroundColor,
            unfocusedBorderColor = BackgroundColor,
            focusedLabelColor = BackgroundColor,
            cursorColor = GrayTextColor,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                isKeyboardVisible = false
            }
        )
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    DisposableEffect(Unit) {
        onDispose {
            // Cleanup, e.g., close keyboard when the composable is disposed
            if (isKeyboardVisible) {
                // Close keyboard
                isKeyboardVisible = false
            }
        }
    }
}