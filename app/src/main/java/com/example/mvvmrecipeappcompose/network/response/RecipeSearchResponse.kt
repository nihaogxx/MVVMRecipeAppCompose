package com.example.mvvmrecipeappcompose.network.response

import com.example.mvvmrecipeappcompose.network.model.RecipeNetworkDTO
import com.google.gson.annotations.SerializedName

data class RecipeSearchResponse (

    @SerializedName("count")
    var count: Int,

    @SerializedName("results")
    var recipes: List<RecipeNetworkDTO>

)
