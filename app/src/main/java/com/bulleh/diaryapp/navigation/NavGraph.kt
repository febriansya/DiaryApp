package com.bulleh.diaryapp.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bulleh.diaryapp.presentation.screens.auth.AuthViewModel
import com.bulleh.diaryapp.presentation.screens.auth.AuthenticationScreen
import com.bulleh.diaryapp.presentation.screens.home.HomeScreen
import com.bulleh.diaryapp.presentation.screens.write.WriteScreen
import com.bulleh.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
) {
    NavHost(
        startDestination = startDestination, navController = navController
    ) {

        authenticationRoute(navigateToHome = {
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        })

        homeRoute(navigateToWrite = {
            navController.navigate(Screen.Write.route)
        })

        writeRoute()

    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        val onTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        AuthenticationScreen(
            loadingState = onTapState.opened,
            authenticated = authenticated,
            onTokenIdReceived = { tokenId ->
                Log.d("token", tokenId)
                viewModel.signInWithMongoAtlas(tokenId, onSuccess = {
                    messageBarState.addSuccess("Successfuly Authenticated!")
                    viewModel.setLoading(false)
                }, onError = {
                    messageBarState.addError(it)
                    viewModel.setLoading(false)
                })

            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)

            },
            oneTapSignInState = onTapState,
            messageBarState = messageBarState,
            onButtonClicked = {
                onTapState.open()
            },
            navigateToHome = navigateToHome
        )
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit
) {
    composable(
        route = Screen.Home.route
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        HomeScreen(
            drawerState = drawerState,
            onSignOutClicked = {
                scope.launch(Dispatchers.IO) {

                }
            },
            oneMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            navigateToWrite = navigateToWrite
        )


    }
}

fun NavGraphBuilder.writeRoute() {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        WriteScreen()
    }
}




