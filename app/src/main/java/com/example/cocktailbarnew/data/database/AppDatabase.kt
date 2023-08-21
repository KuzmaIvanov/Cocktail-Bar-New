package com.example.cocktailbarnew.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cocktailbarnew.data.database.dao.CocktailDao
import com.example.cocktailbarnew.data.database.entity.CocktailEntity
import com.example.cocktailbarnew.data.mapper.StringListConverter

@Database(
    entities = [CocktailEntity::class],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getCocktailDao(): CocktailDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val Lock = Any()
        private const val DATABASE_NAME = "cocktail-bar-new"

        operator fun invoke(context: Context) = instance ?: synchronized(Lock) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }
}