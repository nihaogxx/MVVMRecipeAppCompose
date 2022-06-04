package com.example.mvvmrecipeappcompose.network.model

import com.example.mvvmrecipeappcompose.domain.models.Recipe
import com.example.mvvmrecipeappcompose.domain.util.DomainMapper

class RecipeNetworkDTOMapper : DomainMapper<RecipeNetworkDTO, Recipe> {

    override fun mapToDomainModel(model: RecipeNetworkDTO): Recipe {
        return Recipe(
            id = model.pk,
            title = model.title,
            featuredImage = model.featured_image,
            rating = model.rating,
            publisher = model.publisher,
            sourceUrl = model.sourceUrl,
            ingredients = model.ingredients,
            dateAdded = model.dateAdded,
            dateUpdated = model.dateUpdated,
        )
    }

    override fun mapFromDomainModel(domainModel: Recipe): RecipeNetworkDTO {
        return RecipeNetworkDTO(
            pk = domainModel.id,
            title = domainModel.title,
            featured_image = domainModel.featuredImage,
            rating = domainModel.rating,
            publisher = domainModel.publisher,
            sourceUrl = domainModel.sourceUrl,
            ingredients = domainModel.ingredients,
            dateAdded = domainModel.dateAdded,
            dateUpdated = domainModel.dateUpdated,
        )
    }

    fun toDomainList(initial: List<RecipeNetworkDTO>): List<Recipe>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Recipe>): List<RecipeNetworkDTO>{
        return initial.map { mapFromDomainModel(it) }
    }

}