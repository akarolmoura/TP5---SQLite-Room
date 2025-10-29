package br.unisanta.approom.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "courses")

data class Course(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String
)
