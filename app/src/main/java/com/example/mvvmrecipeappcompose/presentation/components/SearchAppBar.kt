package com.example.mvvmrecipeappcompose.presentation.components

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.mvvmrecipeappcompose.presentation.ui.recipe_list.FoodCategory

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchAppBar(
    query: String,
    setQuery: (String) -> Unit,
    onExecuteSearch: () -> Unit,
    selectedCategory: FoodCategory?,
    onChangeSelectedCategory: (String) -> Unit,
    scrollPosition: Float,
    onCategoryScrollPositionChanged: (Float) -> Unit,
    onToggleTheme: () -> Unit,
) {

    Surface( // create a custom toolbar
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.secondary,
        elevation = 8.dp
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth() // maybe not necessary
            ) {
                val keyboardController = LocalSoftwareKeyboardController.current
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .padding(8.dp),
                    value = query, // initializing value
                    // value = _query.value
                    onValueChange = {
                        // user input changes
                        setQuery(it)
                        // _query.value = it
                    },
                    label = {
                        Text(text = "Search")// floating hint
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "search icon"
                        )
                    },
                    textStyle = MaterialTheme.typography.button
                    ,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onExecuteSearch()
                            keyboardController?.hide()
                        }
                    ),
                )


                ConstraintLayout(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    val (menu) = createRefs()
                    IconButton(
                        modifier = Modifier
                            .constrainAs(menu) {
                                end.linkTo(parent.end)
                                linkTo(top = parent.top, bottom = parent.bottom)
                            },
                        onClick = onToggleTheme
                        ,
                    ){
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "search icon"
                        )
                    }
                }
            }

            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 8.dp, top = 8.dp)
                    .horizontalScroll(scrollState)
            ) {
                // restore scroll position after rotation
                // To call suspend functions safely from inside a composable, use the LaunchedEffect composable
                // When LaunchedEffect enters the Composition, it launches a coroutine with the block of code passed as a parameter.
                LaunchedEffect(scrollState) {
                    scrollState.scrollTo(scrollPosition.toInt())
                }

                for (category in FoodCategory.values()) { // -> FoodCategory
                    Log.d("list", "create chip for ${category.value}")
                    FoodCategoryChip(
                        category = category.value, // -> String
                        isSelected = selectedCategory == category,
                        onSelectedCategoryChanged = {
                            // it = category as a constructor argument
                            onChangeSelectedCategory(it)
                            onCategoryScrollPositionChanged(scrollState.value.toFloat())
                        },
                        // delegate to newSearch fun in viewModel
                        onExecuteSearch = onExecuteSearch
                    )
                }
            }
        }
    }
}


