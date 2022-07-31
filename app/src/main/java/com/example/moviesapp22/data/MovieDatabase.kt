package com.example.moviesapp22.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movie::class, FavouriteMovie::class], version = 3, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "movies.db"
        private var database: MovieDatabase? = null
        private val LOCK : Any = Any()

        fun getInstance(context: Context): MovieDatabase {
            val tempDatabase = database
            if(tempDatabase != null) {
                return tempDatabase
            }
            return database
                ?: synchronized(LOCK) {

                     val databaseInstance =
                        Room.databaseBuilder(context.applicationContext, MovieDatabase::class.java, DB_NAME).fallbackToDestructiveMigration().build()
                    database = databaseInstance
                    return databaseInstance
                           }
        }
    }

    abstract fun movieDao(): MovieDao

}