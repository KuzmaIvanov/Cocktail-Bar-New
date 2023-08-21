package com.example.cocktailbarnew.data.mapper

import com.example.cocktailbarnew.data.database.entity.CocktailEntity
import com.example.cocktailbarnew.domain.model.Cocktail
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CocktailMapper @Inject constructor() {
    fun mapToEntity(cocktail: Cocktail): CocktailEntity = with(cocktail) {
        CocktailEntity(
            id = id,
            name = name,
            description = description,
            recipe = recipe,
            ingredients = ingredients,
            image = image
        )
    }

    fun mapToDomain(cocktailEntity: CocktailEntity) = with(cocktailEntity) {
        Cocktail(
            id = id,
            name = name,
            description = description,
            recipe = recipe,
            ingredients = ingredients,
            image = image
        )
    }
}