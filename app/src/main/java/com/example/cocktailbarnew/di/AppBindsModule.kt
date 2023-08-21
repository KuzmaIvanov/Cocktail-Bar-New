package com.example.cocktailbarnew.di

import com.example.cocktailbarnew.data.repository.CocktailRepositoryImpl
import com.example.cocktailbarnew.domain.repository.CocktailRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppBindsModule {

    @Singleton
    @Binds
    abstract fun bindCocktailRepository(
        cocktailRepositoryImpl: CocktailRepositoryImpl
    ): CocktailRepository
}