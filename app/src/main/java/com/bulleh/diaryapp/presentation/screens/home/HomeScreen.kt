package com.bulleh.diaryapp.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bulleh.diaryapp.R
import com.bulleh.diaryapp.data.repository.Diaries
import com.bulleh.diaryapp.util.RequestState


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    diaries: Diaries,
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    oneMenuClicked: () -> Unit,
    navigateToWrite: () -> Unit
) {
    NavigationDrawer(drawerState = drawerState, onSignOutClicked = onSignOutClicked) {
        Scaffold(topBar = {
            HomeTopBar(oneMenuClicked)
        }, floatingActionButton = {
            FloatingActionButton(onClick = navigateToWrite) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "New Diary Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }, content = {
            /*
            * put content home here
            * */
            when (diaries) {
                is RequestState.Success -> {
                    HomeContent(
                        paddingValues = it,
                        diariesNotes = diaries.data,
                        onClick = {}
                    )
                }

                is RequestState.Error -> {
                    EmptyPage(
                        title = "Error",
                        subtitle = "${diaries.error.message}"
                    )
                }

                is RequestState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    EmptyPage(
                        title =  "Somting wrong"
                    )
                }
            }
        })
    }
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.padding(0.dp, 0.dp, 50.dp, 0.dp),
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.size(250.dp),
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "logo image"
                        )
                    }
                    NavigationDrawerItem(
                        label = {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp)
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.google_logo),
                                    contentDescription = "google logo",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = "Sign Out")
                            }
                        },
                        selected = false,
                        onClick = onSignOutClicked
                    )
                })
        }, content = {
            content()
        }
    )
}
