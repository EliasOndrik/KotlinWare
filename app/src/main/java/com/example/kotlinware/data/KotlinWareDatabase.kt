package com.example.kotlinware.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Player::class, Minigame::class], version = 1, exportSchema = false)
abstract class KotlinWareDatabase : RoomDatabase() {

    abstract fun playerDao() : PlayerDao
    abstract fun minigameDao() : MinigameDao

    companion object {
        @Volatile
        private var Instance: KotlinWareDatabase? = null
        private val minigamesData = listOf(
            "dragging",
            "rotating",
            "swiping",
            "tapping",
        )
        val callback = object : Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                minigamesData.forEach { name ->
                    db.execSQL("INSERT INTO minigames (name,score,unlocked) VALUES ('$name', 0, 0)")
                }
                db.execSQL("INSERT INTO players (money) VALUES (0)")
            }
        }
        fun getDatabase(context: Context): KotlinWareDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    KotlinWareDatabase::class.java,
                    "kotlinware_database")
                    .addCallback(callback)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}