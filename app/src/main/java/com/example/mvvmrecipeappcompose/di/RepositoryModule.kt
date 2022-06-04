package com.example.mvvmrecipeappcompose.di

import com.example.mvvmrecipeappcompose.network.RetrofitService
import com.example.mvvmrecipeappcompose.network.model.RecipeNetworkDTOMapper
import com.example.mvvmrecipeappcompose.repository.RecipeRepository
import com.example.mvvmrecipeappcompose.repository.RecipeRepository_Impl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideRecipeRepository(
        recipeService: RetrofitService,
        recipeMapper: RecipeNetworkDTOMapper
    ): RecipeRepository{
        return RecipeRepository_Impl(recipeService, recipeMapper)
    }

}