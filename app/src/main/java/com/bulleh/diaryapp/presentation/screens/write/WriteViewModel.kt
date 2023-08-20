package com.bulleh.diaryapp.presentation.screens.write

import com.bulleh.diaryapp.model.Diary
import com.bulleh.diaryapp.model.Mood
import io.realm.kotlin.types.RealmInstant

class WriteViewModel {

}

data class UiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,
    val updatedDateTime: RealmInstant? = null
)