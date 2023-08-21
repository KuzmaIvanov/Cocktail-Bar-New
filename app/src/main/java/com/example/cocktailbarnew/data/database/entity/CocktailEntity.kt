package com.example.cocktailbarnew.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cocktails")
data class CocktailEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "recipe") val recipe: String,
    @ColumnInfo(name = "ingredients") val ingredients: List<String>,
    @ColumnInfo(name = "image") val image: String?
)