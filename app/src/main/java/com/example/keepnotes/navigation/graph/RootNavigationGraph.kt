package com.example.keepnotes.navigation.graph

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.keepnotes.R
import com.example.keepnotes.data.auth.GoogleUser
import com.example.keepnotes.data.auth.OneTapSignInWithGoogle
import com.example.keepnotes.data.auth.SignInResult
import com.example.keepnotes.data.auth.getUserFromTokenId
import com.example.keepnotes.data.auth.rememberOneTapSignInState
import com.example.keepnotes.navigation.screen.Screen
import com.example.keepnotes.presentation.screen.loginscreen.LoginScreen
import com.example.keepnotes.presentation.screen.loginscreen.LoginViewModel
import com.example.keepnotes.presentation.screen.loginscreen.SignInViewModel
import com.example.keepnotes.presentation.screen.splash.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun RootNavigationGraph(navHostController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()




    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route){
            SplashScreen(navController = navHostController)
        }

        composable(route = Screen.Login.route){
            lateinit var loginViewModel : LoginViewModel
            val oneTapSignInState = rememberOneTapSignInState()
            var user: GoogleUser? by remember { mutableStateOf(null) }
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            OneTapSignInWithGoogle(
                state = oneTapSignInState,
                clientId = context.getString(R.string.web_client_id),
                rememberAccount = true,
                onTokenIdReceived = {
                    user = getUserFromTokenId(tokenId = it)
                    viewModel.onSignInResult(SignInResult(data = user, errorMessage = null))
                    Log.d("MainActivity", user.toString())
                },
                onDialogDismissed = {
                    viewModel.onSignInResult(SignInResult(data = null, errorMessage = it))
                    Log.d("MainActivity", it)
                }
            )

            LaunchedEffect(key1 = user) {
                user?.let {
                    loginViewModel = LoginViewModel(userData = it)
                    navHostController.navigate(Graph.MAIN)
                }
            }

//            val launcher = rememberLauncherForActivityResult(
//                contract = ActivityResultContracts.StartIntentSenderForResult(),
//                onResult = { result ->
//                    if(result.resultCode == Activity.RESULT_OK) {
//                        scope.launch {
//                            val signInResult = googleAuthUiClient.signInWithIntent(
//                                intent = result.data ?: return@launch
//                            )
//                            viewModel.onSignInResult(signInResult)
//                        }
//                    }
//                }
//            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if(state.isSignInSuccessful) {
                    Toast.makeText(
                        context.applicationContext,
                        "Sign in successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    user?.let {
                        loginViewModel = LoginViewModel(userData = it)
                    }

                    navHostController.navigate(Graph.MAIN)
                    viewModel.resetState()
                }
            }

            LoginScreen(
                state = state,
                onSignInClick = {
                    oneTapSignInState.open()
                    scope.launch {
//                        val signInIntentSender = googleAuthUiClient.signIn()
//                        launcher.launch(
//                            IntentSenderRequest.Builder(
//                                signInIntentSender ?: return@launch
//                            ).build()
//                        )
                    }
                }
            )
        }

        composable(route = Graph.MAIN){
            MainNavGraph()
        }
    }
}