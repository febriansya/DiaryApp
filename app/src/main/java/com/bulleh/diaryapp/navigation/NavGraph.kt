package com.bulleh.diaryapp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bulleh.diaryapp.presentation.screens.auth.AuthViewModel
import com.bulleh.diaryapp.presentation.screens.auth.AuthenticationScreen
import com.bulleh.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState


@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
) {
    NavHost(
        startDestination = startDestination, navController = navController
    ) {
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState

        val onTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        AuthenticationScreen(loadingState = onTapState.opened,
            onTokenIdReceived = { tokenId ->
                Log.d("token",tokenId)
            viewModel.signInWithMongoAtlas(tokenId, onSuccess = {
                messageBarState.addSuccess("Successfuly Authenticated!")
                viewModel.setLoading(false)
            }, onError = {
                messageBarState.addError(it)
            })

        }, onDialogDismissed = { message ->
            messageBarState.addError(Exception(message))

        }, onTapState, messageBarState = messageBarState, onButtonClicked = {
            onTapState.open()
        })
    }
}


fun NavGraphBuilder.homeRoute() {
    composable(
        route = Screen.Home.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

    }
}

fun NavGraphBuilder.writeRoute() {
    composable(route = Screen.Write.route) {

    }
}




