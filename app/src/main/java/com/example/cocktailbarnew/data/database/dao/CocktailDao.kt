package com.example.cocktailbarnew.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cocktailbarnew.data.database.entity.CocktailEntity

@Dao
interface CocktailDao {
    @Query("SELECT * FROM cocktails")
    suspend fun getAll(): List<CocktailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cocktailEntity: CocktailEntity)

    @Query("DELETE FROM cocktails WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM cocktails WHERE id = :id")
    suspend fun getById(id: Long): CocktailEntity
}