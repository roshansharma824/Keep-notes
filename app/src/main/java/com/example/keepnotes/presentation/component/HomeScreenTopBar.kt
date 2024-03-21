package com.example.keepnotes.presentation.component

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.keepnotes.MainActivity
import com.example.keepnotes.R
import com.example.keepnotes.data.auth.GoogleAuthUiClient
import com.example.keepnotes.data.local.InMemoryCache
import com.example.keepnotes.presentation.common.ProgressIndicator
import com.example.keepnotes.presentation.screen.loginscreen.LoginViewModel
import com.example.keepnotes.presentation.screen.loginscreen.SignInViewModel
import com.example.keepnotes.ui.theme.BackgroundColor
import com.example.keepnotes.ui.theme.DIMENS_16dp
import com.example.keepnotes.ui.theme.DIMENS_20dp
import com.example.keepnotes.ui.theme.DIMENS_24dp
import com.example.keepnotes.ui.theme.DIMENS_8dp
import com.example.keepnotes.ui.theme.TEXT_SIZE_18sp
import com.example.keepnotes.ui.theme.TopBarBackgroundColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch


@Composable
fun HomeScreenTopBar(
    onClickAction: () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(BackgroundColor)
    }
    val context = LocalContext.current
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context.applicationContext,
            oneTapClient = Identity.getSignInClient(context.applicationContext)
        )
    }
    val scope = rememberCoroutineScope()
    lateinit var loginViewModel: LoginViewModel
    val viewModel = viewModel<SignInViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var isLoading by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            Toast.makeText(
                context.applicationContext,
                "Sign in successful",
                Toast.LENGTH_SHORT
            ).show()
            loginViewModel = LoginViewModel(googleAuthUiClient.getSignedInUser()!!)
            val refresh = Intent(context, MainActivity::class.java)
            context.startActivity(refresh)
            viewModel.resetState()
        }

    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                scope.launch {
                    isLoading = true
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult)
                }
            }
            isLoading = false
        }
    )

    Box(
        modifier = Modifier.padding(horizontal = DIMENS_16dp, vertical = DIMENS_8dp)
    ) {


        TopAppBar(
            backgroundColor = Color.Transparent,
            contentColor = Color.Transparent,
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_horizontal_grid),
                        contentDescription = "Grid Icon",
                        tint = Color.White,
                        modifier = Modifier.size(
                            DIMENS_20dp
                        )
                    )
                }
                AsyncImage(
                    model = "${InMemoryCache.userData.profilePictureUrl}",
                    contentDescription = "profile img",
                    contentScale = ContentScale.Fit,            // crop the image if it's not a square
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)                       // clip to the circle shape
                        .border(1.dp, Color.Gray, CircleShape)
                        .clickable {
                            scope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest
                                        .Builder(
                                            signInIntentSender ?: return@launch
                                        )
                                        .build()
                                )
                            }
                            isLoading = true
                        }   // add a border (optional)
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
            modifier = Modifier
                .background(
                    color = TopBarBackgroundColor,
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = DIMENS_8dp),
            navigationIcon = {
                IconButton(onClick = { onClickAction.invoke() }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
            }
        )
    }
    if (isLoading){
        ProgressIndicator()
    }
}