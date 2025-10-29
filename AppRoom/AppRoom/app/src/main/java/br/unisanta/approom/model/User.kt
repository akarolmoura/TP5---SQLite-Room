package br.unisanta.approom.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")

data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val age: Int,

    val phone: String,

    val courseId: Int,

    val password: String
)
