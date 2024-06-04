package com.example.task
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.UUID


open class User(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var username: String = "",
    var password: String = ""
) : RealmObject()
