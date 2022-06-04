package com.example.mvvmrecipeappcompose.di

import android.content.Context
import com.example.mvvmrecipeappcompose.presentation.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent :: class) // inject for Application
object AppModule{ // object -> singleton
    @Singleton
    @Provides // use when building a function to provide dependency
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }
}