package com.example.mvvmrecipeappcompose.presentation.ui.recipe_list

import android.inputmethodservice.Keyboard
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mvvmrecipeappcompose.R
import com.example.mvvmrecipeappcompose.presentation.BaseApplication
import com.example.mvvmrecipeappcompose.presentation.components.CircularProgressBar
import com.example.mvvmrecipeappcompose.presentation.components.FoodCategoryChip
import com.example.mvvmrecipeappcompose.presentation.components.RecipeCard
import com.example.mvvmrecipeappcompose.presentation.components.SearchAppBar
import com.example.mvvmrecipeappcompose.presentation.theme.AppTheme
import com.example.mvvmrecipeappcompose.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var application: BaseApplication

    private val viewModel: RecipeListViewModel by activityViewModels() // Or by viewModels(). this is to share viewModel between diff fragments

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //When using LiveData:
        //viewModel.recipes.observe(viewLifecycleOwner, {recipes -> ...})
        return ComposeView(requireContext()).apply {
            setContent {

                AppTheme(
                    darkTheme = application.isDark.value
                ) {// kotlin convention: the last argument[content()] in a function is passed as a function to the Lambda
                    // When using MutableState
                    val recipes = viewModel.recipes.value
                    val query = viewModel.query.value
                    val selectedCategory = viewModel.selectedCategory.value
                    val categoryScrollPosition = viewModel.categoryScrollPosition
                    val loading = viewModel.loading.value

                    // using saveInstanceState
                    //val _query = savedInstanceState{"beef"}

                    // cannot survive configuration change
                    //val _query = remember { mutableStateOf("beef")}

                    Column {
                        SearchAppBar(
                            query = query,
                            setQuery = viewModel::setQuery,
                            onExecuteSearch = viewModel::newSearch,
                            selectedCategory = selectedCategory,
                            setSelectedCategory = viewModel::setSelectedCategory,
                            scrollPosition = categoryScrollPosition,
                            setScrollPosition = viewModel::setScrollPosition,
                            onToggleTheme = {
                                application.toggleLightTheme()
                            }
                        )

                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .background(MaterialTheme.colors.background)
                        ) {
                            LazyColumn { // or define content as a constructor argument LazyColumn(content = {...})
                                itemsIndexed(
                                    items = recipes
                                ) { index, recipe ->
                                    RecipeCard(recipe = recipe, onClick = {})

                                }
                            }

                            CircularProgressBar(isDisplayed = loading)
                        }

                    }
                }


            }

        }
    }
}
