package com.example.mvvmrecipeappcompose.presentation.ui.recipe

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmrecipeappcompose.domain.models.Recipe
import com.example.mvvmrecipeappcompose.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_RECIPE = "recipe.state.recipe.key"

@HiltViewModel
class RecipeViewModel
@Inject
constructor(
    private val recipeRepository: RecipeRepository,
    private @Named("auth_token") val token: String,
    private val state: SavedStateHandle,
): ViewModel(){

    val recipe: MutableState<Recipe?> = mutableStateOf(null)

    val loading = mutableStateOf(false)

    init {
        // restore if process dies
        state.get<Int>(STATE_KEY_RECIPE)?.let{ recipeId ->
            getRecipe(recipeId)
        }
    }

    fun getRecipe(recipeId: Int) {
        viewModelScope.launch {
            loading.value = true

            // simulate a delay to show loading
            delay(1000)

            val result = recipeRepository.get(token=token, id = recipeId)
            recipe.value = result

            state.set(STATE_KEY_RECIPE, result.id)

            loading.value = false
        }
    }
}