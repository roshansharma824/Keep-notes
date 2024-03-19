package com.example.keepnotes.navigation.graph

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.keepnotes.data.auth.GoogleAuthUiClient

import com.example.keepnotes.navigation.screen.Screen
import com.example.keepnotes.presentation.MainScreen
import com.example.keepnotes.presentation.screen.loginscreen.LoginViewModel
import com.example.keepnotes.presentation.screen.loginscreen.LoginScreen
import com.example.keepnotes.presentation.screen.loginscreen.SignInViewModel
import com.example.keepnotes.presentation.screen.splash.SplashScreen
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

@Composable
fun RootNavigationGraph(navHostController: NavHostController) {
    val context = LocalContext.current
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context.applicationContext,
            oneTapClient = Identity.getSignInClient(context.applicationContext)
        )
    }
    val scope = rememberCoroutineScope()
    lateinit var loginViewModel : LoginViewModel

    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route){
            SplashScreen(navController = navHostController)
        }

        composable(route = Screen.Login.route){
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = googleAuthUiClient.getSignedInUser()) {
                if(googleAuthUiClient.getSignedInUser() != null) {
                    loginViewModel = LoginViewModel(googleAuthUiClient.getSignedInUser()!!)
                    navHostController.navigate(Graph.MAIN)
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if(result.resultCode == Activity.RESULT_OK) {
                        scope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if(state.isSignInSuccessful) {
                    Toast.makeText(
                        context.applicationContext,
                        "Sign in successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginViewModel = LoginViewModel(googleAuthUiClient.getSignedInUser()!!)
                    navHostController.navigate(Graph.MAIN)
                    viewModel.resetState()
                }
            }

            LoginScreen(
                state = state,
                onSignInClick = {
                    scope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }

        composable(route = Graph.MAIN){
            MainScreen()
        }
    }
}