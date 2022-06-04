package com.example.mvvmrecipeappcompose.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.unit.dp
import com.example.mvvmrecipeappcompose.domain.models.Recipe
import com.example.mvvmrecipeappcompose.util.DEFUALT_RECIPE_IMAGE
import com.example.mvvmrecipeappcompose.util.loadImage

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit
){
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                bottom = 6.dp,
                top = 6.dp
            )
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 8.dp
        /*to customize a shape
        shape = CornerBasedShape(
            topEnd = CornerSize(5.dp),
            topStart = CornerSize(5.dp),
            bottomEnd = CornerSize(5.dp),
            bottomStart = CornerSize(5.dp)
        )*/
    ){
        Column {
            recipe.featuredImage.let { url ->
                val image = loadImage(url = url, defaultImage = DEFUALT_RECIPE_IMAGE)
                image.let{ img ->
                    img.value?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Dish Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(225.dp),// preferredHeight?
                            contentScale = ContentScale.Crop
                        )
                    }

                }
            }

            recipe.title.let { title ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 12.dp, start = 8.dp, end = 8.dp)
                ){
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth(0.85f) // fill the row to 85%
                            .wrapContentWidth(Alignment.Start),
                        style = MaterialTheme.typography.h3
                    )
                    Text(
                        text = recipe.rating.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.h5
                    )
                }
            }
        }
    }
}