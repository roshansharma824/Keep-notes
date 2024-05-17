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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.keepnotes.R
import com.example.keepnotes.data.auth.GoogleUser
import com.example.keepnotes.data.auth.OneTapSignInWithGoogle
import com.example.keepnotes.data.auth.SignInResult
import com.example.keepnotes.data.auth.getUserFromTokenId
import com.example.keepnotes.data.auth.rememberOneTapSignInState
import com.example.keepnotes.data.local.InMemoryCache
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
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navHostController)
        }

        composable(route = Screen.Login.route) {
            lateinit var loginViewModel: LoginViewModel
            val oneTapSignInState = rememberOneTapSignInState()
            var user: GoogleUser? by remember { mutableStateOf(null) }
            val viewModel = hiltViewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val userId by viewModel.userId.collectAsStateWithLifecycle()
            val userProfileUrl by viewModel.userProfileUrl.collectAsStateWithLifecycle()


            OneTapSignInWithGoogle(
                state = oneTapSignInState,
                clientId = context.getString(R.string.web_client_id),
                rememberAccount = true,
                onTokenIdReceived = {
                    user = getUserFromTokenId(tokenId = it)
                    scope.launch {
                        user?.sub?.let { userId ->
                            viewModel.saveUserId(userId)
                        }
                        user?.picture?.let { it1 ->
                            viewModel.saveUserProfileUrl(it1)
                        }
                    }

                    viewModel.onSignInResult(SignInResult(data = user, errorMessage = null))
                    Log.d("RootNavigationGraph", user.toString())
                },
                onDialogDismissed = {
                    viewModel.onSignInResult(SignInResult(data = null, errorMessage = it))
                    Log.d("RootNavigationGraph", it)
                }
            )

            LaunchedEffect(key1 = user) {
                Log.d("RootNavigationGraph1", userId)
                user?.let {
                    InMemoryCache.userData.userId = userId
                    InMemoryCache.userData.profilrUrl = userProfileUrl
                    Log.d("RootNavigationGraph11", user.toString())
                    loginViewModel = LoginViewModel(userData = user)
                    navHostController.navigate(Graph.MAIN)
                }
            }

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                Log.d("RootNavigationGraph2", userId)
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        context.applicationContext,
                        "Sign in successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("RootNavigationGraph21", userId)
                    loginViewModel = LoginViewModel(userData = user)
                    navHostController.navigate(Graph.MAIN)
                    viewModel.resetState()
                }
            }

            LoginScreen(
                state = state,
                onSignInClick = {
                    oneTapSignInState.open()
                }
            )
        }

        composable(route = Graph.MAIN) {
            MainNavGraph()
        }
    }
}