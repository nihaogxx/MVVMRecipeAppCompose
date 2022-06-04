package com.example.mvvmrecipeappcompose.repository

import com.example.mvvmrecipeappcompose.domain.models.Recipe
import com.example.mvvmrecipeappcompose.network.RetrofitService
import com.example.mvvmrecipeappcompose.network.model.RecipeNetworkDTOMapper

class RecipeRepository_Impl(
    private val recipeService: RetrofitService,
    private val mapper: RecipeNetworkDTOMapper
) : RecipeRepository{

    override suspend fun search(token: String, page: Int, query: String): List<Recipe> {
        val result = recipeService.search(token, page, query).recipes // DTO list
        return mapper.toDomainList(result);

    }

    override suspend fun get(token: String, id: Int): Recipe {
        val result = recipeService.get(token, id)
        return mapper.mapToDomainModel(result)
    }
}