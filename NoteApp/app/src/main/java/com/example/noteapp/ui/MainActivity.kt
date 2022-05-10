package com.example.noteapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.noteapp.ui.gl.OpenGLES20Activity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteApp(application = application)
        }

        startActivity(Intent(this@MainActivity, OpenGLES20Activity::class.java))

    }
}