package com.example.mvvmrecipeappcompose.presentation

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // triggers Hilt's code generation, including a base class for your application that serves as the application-level dependency container.
class BaseApplication: Application(){
    // should be saved in data store
    val isDark = mutableStateOf(false)

    fun toggleLightTheme(){
        isDark.value = !isDark.value
    }
}