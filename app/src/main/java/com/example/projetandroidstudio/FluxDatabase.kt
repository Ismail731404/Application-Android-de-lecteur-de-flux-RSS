package com.example.projetandroidstudio

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Flux::class, Info::class], version = 1, exportSchema = false)
public abstract class FluxDatabase : RoomDatabase() {

    abstract fun fluxDao(): FluxDao
    abstract fun infoDao(): InfoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: FluxDatabase? = null

        fun getDatabase(context: Context): FluxDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FluxDatabase::class.java,
                    "word_database"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}