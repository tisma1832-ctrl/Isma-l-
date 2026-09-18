package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ChatEntity::class, ChatMessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class KomdeDatabase : RoomDatabase() {
    abstract fun komdeDao(): KomdeDao

    companion object {
        @Volatile
        private var INSTANCE: KomdeDatabase? = null

        fun getInstance(context: Context): KomdeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KomdeDatabase::class.java,
                    "komde_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
