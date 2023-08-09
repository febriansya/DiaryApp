package com.bulleh.diaryapp.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    oneMenuClicked: () -> Unit,
    navigateToWrite: () -> Unit
) {
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
    },
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        Text(text = "halo ini adalah home")
    }
}


@Preview
@Composable
fun previewHome() {
    HomeScreen({}, {})
}