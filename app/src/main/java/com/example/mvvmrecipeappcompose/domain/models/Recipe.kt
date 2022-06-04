package com.example.mvvmrecipeappcompose.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id : Int,
    val title:String,
    val publisher:String,
    val featuredImage:String,
    val rating:Int = 0,
    val sourceUrl:String,
    val ingredients : List<String> = listOf(),
    val dateAdded: String,
    val dateUpdated: String,
    ) : Parcelable





