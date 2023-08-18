package com.bulleh.diaryapp.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bulleh.diaryapp.data.repository.MongoDB
import com.bulleh.diaryapp.presentation.components.DisplayAlertDialog
import com.bulleh.diaryapp.presentation.screens.auth.AuthViewModel
import com.bulleh.diaryapp.presentation.screens.auth.AuthenticationScreen
import com.bulleh.diaryapp.presentation.screens.home.HomeScreen
import com.bulleh.diaryapp.presentation.screens.home.HomeViewModel
import com.bulleh.diaryapp.presentation.screens.write.WriteScreen
import com.bulleh.diaryapp.util.Constants.APP_ID
import com.bulleh.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun SetupNavGraph(
    startDestination: String, navController: NavHostController, onDataLoaded: () -> Unit
) {
    NavHost(
        startDestination = startDestination, navController = navController
    ) {

        /*
        *
        * this code for auth route
        * */
        authenticationRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }, onDataLoaded = onDataLoaded
        )

        /*
        * this code for home route
        * */
        homeRoute(navigateToWrite = {
            navController.navigate(Screen.Write.route)
        }, navigateToAuth = {
            navController.popBackStack()
            navController.navigate(Screen.Authentication.route)
        }, onDataLoaded = onDataLoaded, navigateToWriteArgs = {
            navController.navigate(Screen.Write.passDiaryId(diaryId = it))
        })

        /*
        * this code for write route
        * */
        writeRoute(navigateBack = {
            navController.popBackStack()
        })

    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit, onDataLoaded: () -> Unit
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
    navigateToWrite: () -> Unit,
    navigateToWriteArgs: (String) -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(
        route = Screen.Home.route
    ) {
        val viewModel: HomeViewModel = viewModel()
        val diaries by viewModel.diaries

        val user = App.create(APP_ID)

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var signOutDialogOpened by remember { mutableStateOf(false) }
        var deleteAllDialogOpened by remember { mutableStateOf(false) }


        HomeScreen(
            diaries = diaries,
            drawerState = drawerState,
            onSignOutClicked = {
                signOutDialogOpened = true
            }, oneMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            }, navigateToWrite = navigateToWrite
        )

        LaunchedEffect(key1 = Unit) {
            MongoDB.configureTheRealm()
        }

        DisplayAlertDialog(title = "Sign Out",
            message = "Are you sure you want to Sign Out from your Google Account?",
            dialogOpen = signOutDialogOpened,
            onDialogClosed = { signOutDialogOpened = false },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.create(APP_ID).currentUser
                    if (user != null) {
                        user.logOut()
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            })
    }
}

fun NavGraphBuilder.writeRoute(
    navigateBack: () -> Unit
) {
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




