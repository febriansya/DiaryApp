package com.bulleh.diaryapp.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bulleh.diaryapp.presentation.screens.auth.AuthViewModel
import com.bulleh.diaryapp.presentation.screens.auth.AuthenticationScreen
import com.bulleh.diaryapp.presentation.screens.home.HomeScreen
import com.bulleh.diaryapp.util.Constants.APP_ID
import com.bulleh.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
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

//        homeRoute()
        authenticationRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }
        )

//        composable(Screen.Home.route){
//            HomeScreen()
//        }
//

        homeRoute()
//        writeRoute()
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


fun NavGraphBuilder.homeRoute() {
    composable(
        route = Screen.Home.route
    ) {
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                scope.launch(Dispatchers.IO) {
                    App.create(APP_ID).currentUser?.logOut()
                }
            }) {
                Text(text = "Logout", color = Color.Black)
            }
        }
    }
}

fun NavGraphBuilder.writeRoute() {
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




