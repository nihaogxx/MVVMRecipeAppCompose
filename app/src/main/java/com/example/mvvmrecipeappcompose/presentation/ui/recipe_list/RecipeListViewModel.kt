package com.example.mvvmrecipeappcompose.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmrecipeappcompose.domain.models.Recipe
import com.example.mvvmrecipeappcompose.repository.RecipeRepository
import com.example.mvvmrecipeappcompose.util.loadImage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 30

const val STATE_KEY_PAGE = "recipe.state.page.key"
const val STATE_KEY_QUERY = "recipe.state.query.key"
const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"
const val STATE_KEY_SELECTED_CATEGORY = "recipe.state.query.selected_category"

@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val repository: RecipeRepository,
    private @Named("auth_token") val token: String,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(){

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())
    val query = mutableStateOf("")
    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)
    val loading = mutableStateOf(false)
    var categoryScrollPosition: Float = 0f
    var recipeListScrollPosition = 0 // no need to react to change in this value, so not making it mutable
    val page = mutableStateOf(1) // prob no need to be mutable for above reason


    // make _recipes accessible from UI but read only
    //val recipes: LiveData<List<Recipe>> get() = _recipes

    init {
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p ->
            setPage(p)
        }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setQuery(q)
        }
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { p ->
            setListScrollPosition(p)
        }
        savedStateHandle.get<FoodCategory>(STATE_KEY_SELECTED_CATEGORY)?.let { c ->
            setSelectedCategory(c)
        }

        // Were they doing something before the process died?
        if(recipeListScrollPosition != 0){
            viewModelScope.launch {
                restoreState()
            }
        }
        else{
            newSearch()
        }

    }

    private suspend fun restoreState(){
        loading.value = true
        // Must retrieve each page of results.
        val results: MutableList<Recipe> = mutableListOf()
        // use cache in real app
        for(p in 1..page.value){
            val result = repository.search(token = token, page = p, query = query.value )
            Log.d("restoreState", "result value: $result")
            results.addAll(result)
            if(p == page.value){ // done
                recipes.value = results
                loading.value = false
            }
        }
    }
    fun newSearch(){
        Log.d("ViewModel", "new search is called: ${query.value}, page: ${page.value}")
        viewModelScope.launch {
            try {
                loading.value = true
                resetSearchState()
                val result = repository.search(
                    token = token,
                    page = page.value,
                    query = query.value)
                Log.d("ViewModel", "result: $result")
                recipes.value = result
                loading.value = false
            }catch (e: Exception){
                Log.e("ViewModel", "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }

        }
    }

    fun nextPage(){
        viewModelScope.launch {
            // prevent duplicate event due to recompose happening to quickly
            if((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE)){
                loading.value = true
                incrementPage()

                delay(1000)

                if(page.value > 1){ // not new search
                    val result = repository.search(token = token, page = page.value, query = query.value )
                    appendRecipes(result)
                }
                loading.value = false
            }
        }
    }

    /**
     * Called when a new search is executed.
     */
    private fun resetSearchState(){
        recipes.value = listOf() // so that screen will scroll to top when display new search result
        onChangeRecipeScrollPosition(0)
        page.value = 1
        if(selectedCategory.value?.value != query.value) clearSelectedCategory()
    }

    private fun appendRecipes(result: List<Recipe>) {
        val current = ArrayList(recipes.value)
        current.addAll(result)
        recipes.value = current
    }

    private fun incrementPage() {
        page.value = page.value + 1
    }

    /**
     * Called when user search in the search bar
     */
    private fun clearSelectedCategory(){
        setSelectedCategory(null)
    }

    fun onQueryChanged(query: String){
        setQuery(query)
    }

    fun onSelectedCategoryChanged(category: String){
//        Log.d("viewmodel", "category value: ${FoodCategory.valueOf(category)}")
        val newCategory = getFoodCategory(category)
        setSelectedCategory(newCategory)
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(position: Float){
        categoryScrollPosition = position
    }

    fun onChangeRecipeScrollPosition(position: Int){
        recipeListScrollPosition = position
    }

    private fun setListScrollPosition(position: Int){
        recipeListScrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }

    private fun setPage(page: Int){
        this.page.value = page
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }

    private fun setSelectedCategory(category: FoodCategory?){
        selectedCategory.value = category
        savedStateHandle.set(STATE_KEY_SELECTED_CATEGORY, category)
    }

    private fun setQuery(query: String){
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }
}

