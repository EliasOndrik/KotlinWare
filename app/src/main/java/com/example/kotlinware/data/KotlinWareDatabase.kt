package com.example.kotlinware.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Player::class, Minigame::class], version = 1, exportSchema = false)
abstract class KotlinWareDatabase : RoomDatabase() {

    abstract fun playerDao() : PlayerDao
    abstract fun minigameDao() : MinigameDao
    companion object {
        @Volatile
        private var Instance: KotlinWareDatabase? = null

        fun getDatabase(context: Context): KotlinWareDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, KotlinWareDatabase::class.java, "kotlinware_database")
                    .build().also { Instance = it }
            }
        }
    }
}