package com.example.cocktailbarnew.data.repository

import com.example.cocktailbarnew.data.database.AppDatabase
import com.example.cocktailbarnew.data.mapper.CocktailMapper
import com.example.cocktailbarnew.domain.model.Cocktail
import com.example.cocktailbarnew.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CocktailRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val mapper: CocktailMapper
) : CocktailRepository {

    override suspend fun getAll(): Flow<List<Cocktail>> = flow {
        emit(db.getCocktailDao().getAll().map { mapper.mapToDomain(it) })
    }

    override suspend fun insert(cocktail: Cocktail) {
        db.getCocktailDao().insert(mapper.mapToEntity(cocktail))
    }

    override suspend fun deleteById(id: Long) {
        db.getCocktailDao().deleteById(id)
    }

    override suspend fun getById(id: Long) = flow {
        emit(mapper.mapToDomain(db.getCocktailDao().getById(id)))
    }
}