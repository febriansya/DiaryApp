package com.bulleh.diaryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bulleh.diaryapp.presentation.screens.auth.AuthenticationScreen
import com.bulleh.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.onetap.rememberOneTapSignInState


@Composable
fun SetupNavGraph(startDestination: String, navController: NavHostController) {
    NavHost(
        startDestination = startDestination,
        navController = navController
    ) {
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute() {
    composable(route = Screen.Authentication.route) {
        val onTapState = rememberOneTapSignInState()
        AuthenticationScreen(loadingState = onTapState.opened, onTapState, onButtonClicked = {
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




