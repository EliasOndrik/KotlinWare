package com.example.kotlinware.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "minigames")
data class Minigame(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val score: Int,
    val unlocked: Boolean = false
)
