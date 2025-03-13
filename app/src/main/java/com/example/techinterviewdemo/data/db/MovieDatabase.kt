// app/src/main/java/com/example/techinterviewdemo/data/db/MovieDatabase.kt
package com.example.techinterviewdemo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}