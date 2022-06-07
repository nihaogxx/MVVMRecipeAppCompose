package com.example.mvvmrecipeappcompose.presentation.ui.recipe_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.mvvmrecipeappcompose.R
import com.example.mvvmrecipeappcompose.presentation.BaseApplication
import com.example.mvvmrecipeappcompose.presentation.components.*
import com.example.mvvmrecipeappcompose.presentation.components.util.SnackbarController
import com.example.mvvmrecipeappcompose.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var application: BaseApplication

    private val snackbarController = SnackbarController(lifecycleScope)

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
                    Log.d("RecipeFragment", "recipe value: $recipes")
                    val query = viewModel.query.value
                    val selectedCategory = viewModel.selectedCategory.value
                    val categoryScrollPosition = viewModel.categoryScrollPosition
                    val loading = viewModel.loading.value
                    val page = viewModel.page.value
                    val scaffoldState = rememberScaffoldState()

                    // using saveInstanceState
                    //val _query = savedInstanceState{"beef"}

                    // cannot survive configuration change
                    //val _query = remember { mutableStateOf("beef")}

                    Scaffold(
                        topBar = {
                            SearchAppBar(
                                query = query,
                                setQuery = viewModel::onQueryChanged,
                                onExecuteSearch = {
                                    if (viewModel.selectedCategory.value?.value == "Milk"){
                                        snackbarController.getScope().launch {

                                            snackbarController.showSnackbar(
                                                scaffoldState = scaffoldState,
                                                message = "Invalid category: MILK",
                                                actionLabel = "Hide"
                                            )
                                        }
                                    }
                                    else{
                                        viewModel.newSearch()
                                    }
                                },
                                selectedCategory = selectedCategory,
                                onChangeSelectedCategory = viewModel::onSelectedCategoryChanged,
                                scrollPosition = categoryScrollPosition,
                                onCategoryScrollPositionChanged = viewModel::onChangeCategoryScrollPosition,
                                onToggleTheme = {
                                    application.toggleLightTheme()
                                }
                            )
                        },
                        bottomBar = {},
                        drawerContent = {},
                        scaffoldState = scaffoldState, // by default
                        snackbarHost = {
                            scaffoldState.snackbarHostState
                        },

                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.background)
                        ) {
                            LazyColumn { // or define content as a constructor argument LazyColumn(content = {...})
                                itemsIndexed(
                                    items = recipes
                                ) { index, recipe ->
                                    viewModel.onChangeRecipeScrollPosition(index)
                                    if((index + 1) >= (page * PAGE_SIZE) && !loading){
                                        viewModel.nextPage()
                                    }
                                    RecipeCard(
                                        recipe = recipe,
                                        onClick = {
                                            if(recipe.id != null){
                                                val bundle = Bundle()
                                                bundle.putInt("recipeId", recipe.id)
                                                findNavController().navigate(R.id.viewRecipe, bundle)
                                            }
                                    })

                                }
                            }

                            CircularProgressBar(isDisplayed = loading)
                            DefaultSnackbar(
                                snackbarHostState = scaffoldState.snackbarHostState,
                                onDismiss = {
                                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                                },
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        }

                    }
                }
            }

        }
    }
}
