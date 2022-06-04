package com.example.mvvmrecipeappcompose.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.mvvmrecipeappcompose.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint // use for any class that you are injecting dependencies to. if a fragment marked with this is hosted on the activity, then activity must be marked with it too.
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var app: BaseApplication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("activity app context: $app")

    }
}