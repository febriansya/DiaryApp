package com.bulleh.diaryapp.presentation.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    oneMenuClicked: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = oneMenuClicked) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "top menu"
                )
            }
        },
        title = {
            Text(text = "Diary")
        },
        actions = {
            IconButton(onClick = oneMenuClicked) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Range"
                )
            }

        }
    )
}


@Preview
@Composable
fun HomeTopBarPreview() {
    HomeTopBar(oneMenuClicked = {})
}