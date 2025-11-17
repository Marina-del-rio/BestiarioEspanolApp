package com.example.bestiario_espaol.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Criatura::class], version = 1)
abstract class CriaturaDatabase : RoomDatabase() {

    abstract fun criaturaDao(): CriaturaDAO

    companion object {
        @Volatile
        private var INSTANCE: CriaturaDatabase? = null

        fun getDatabase(context: Context): CriaturaDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CriaturaDatabase::class.java,
                    "criatura_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}