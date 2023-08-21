package com.example.cocktailbarnew.domain.repository

import com.example.cocktailbarnew.domain.model.Cocktail
import kotlinx.coroutines.flow.Flow

interface CocktailRepository {

    suspend fun getAll(): Flow<List<Cocktail>>

    suspend fun insert(cocktail: Cocktail)

    suspend fun deleteById(id: Long)

    suspend fun getById(id: Long): Flow<Cocktail>
}