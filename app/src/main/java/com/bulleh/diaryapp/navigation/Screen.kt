package com.bulleh.diaryapp.navigation

import com.bulleh.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT_KEY

// settings route screen

sealed class Screen(val route: String) {
    object Authentication : Screen("authentication_screen")
    object Home : Screen("home_screen")
    object Write : Screen(
        "write_screen?$WRITE_SCREEN_ARGUMENT_KEY=" +
                "{$WRITE_SCREEN_ARGUMENT_KEY}"
    ) {
        fun passDiaryId(diaryId: String) = "write_screen?diaryId=$diaryId"
    }
}