package com.bulleh.diaryapp.data.repository

import com.bulleh.diaryapp.model.Diary
import com.bulleh.diaryapp.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>

interface MongoRepository {
    fun configureTheRealm()
    fun getAllDiaries(): Flow<Diaries>
    fun getSelectedDiary(diaryId: ObjectId): RequestState<Diary>

    suspend fun addNewDiary(diary: Diary): RequestState<Diary>
}