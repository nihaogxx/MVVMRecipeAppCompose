package com.example.mvvmrecipeappcompose.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.mvvmrecipeappcompose.presentation.BaseApplication
import com.example.mvvmrecipeappcompose.presentation.components.CircularProgressBar
import com.example.mvvmrecipeappcompose.presentation.components.DefaultSnackbar
import com.example.mvvmrecipeappcompose.presentation.components.RecipeView
import com.example.mvvmrecipeappcompose.presentation.components.util.SnackbarController
import com.example.mvvmrecipeappcompose.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalMaterialApi::class)
@AndroidEntryPoint
class RecipeFragment : Fragment() {

    @Inject
    lateinit var application: BaseApplication
    private val snackbarController = SnackbarController(lifecycleScope)
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("recipeId")?.let { recipeId ->
            viewModel.getRecipe(recipeId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val loading = viewModel.loading.value

                val recipe = viewModel.recipe.value

                val scaffoldState = rememberScaffoldState()

                AppTheme(darkTheme = application.isDark.value) {
                    Scaffold(
                        scaffoldState = scaffoldState,
                        snackbarHost = {
                            scaffoldState.snackbarHostState
                        }
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (loading && recipe == null) {
                                // load shimmer
                            } else recipe?.let {
                                RecipeView(recipe = it)
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