package com.example.cocktailbarnew.di

import android.content.Context
import com.example.cocktailbarnew.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase(context)
    }
}