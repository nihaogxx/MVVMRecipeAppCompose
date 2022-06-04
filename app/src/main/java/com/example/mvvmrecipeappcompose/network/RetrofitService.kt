package com.example.mvvmrecipeappcompose.network

import com.example.mvvmrecipeappcompose.network.model.RecipeNetworkDTO
import com.example.mvvmrecipeappcompose.network.response.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService{

    @GET("search")
    suspend fun search(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): RecipeSearchResponse

    @GET("get")
    suspend fun get(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): RecipeNetworkDTO
}