package com.example.diaryapp.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Diary : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId.create()
    var ownerId: String = ""
    var mod: String = Mood.Neutral.name
    var title: String = ""
    var description: String = ""
    var image: RealmList<String> = realmListOf()
    var data: RealmInstant = RealmInstant.from(System.currentTimeMillis(), 0)
}