package com.example.kotlinware.data

import androidx.room.Entity

@Entity
data class Player(
    val id: Int,
    val money: Int
)
