package com.example.mvvmrecipeappcompose.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmrecipeappcompose.domain.models.Recipe
import com.example.mvvmrecipeappcompose.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RecipeListViewModel
@Inject constructor(
    private val repository: RecipeRepository,
    private @Named("auth_token") val token: String
) : ViewModel(){

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())
    val query = mutableStateOf("")
    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)
    var categoryScrollPosition: Float = 0f
    val loading = mutableStateOf(false)

    // make _recipes accessible from UI but read only
    //val recipes: LiveData<List<Recipe>> get() = _recipes

    init {
        newSearch()
    }

    fun newSearch(){
        viewModelScope.launch {
            loading.value = true
            resetSearchState()
            val result = repository.search(
                token = token,
                page = 1,
                query = query.value)
            recipes.value = result
            loading.value = false

        }
    }

    /**
     * Called when a new search is executed.
     */
    private fun resetSearchState(){
        // so that screen will scroll to top when display new search result
        recipes.value = listOf()
        if(selectedCategory.value?.value != query.value) clearSelectedCategory()
    }

    /**
     * Called when user search in the search bar
     */
    private fun clearSelectedCategory(){
        selectedCategory.value = null
    }

    fun setQuery(query: String){
        this.query.value = query
    }

    fun setSelectedCategory(category: String){
//        Log.d("viewmodel", "category value: ${FoodCategory.valueOf(category)}")
        val newCategory = getFoodCategory(category)
        selectedCategory.value = newCategory
        setQuery(category)
    }

    fun setScrollPosition(position: Float){
        categoryScrollPosition = position
    }

}