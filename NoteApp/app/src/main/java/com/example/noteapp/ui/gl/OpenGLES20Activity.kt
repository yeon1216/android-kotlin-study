package com.example.noteapp.ui.gl

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class OpenGLES20Activity : AppCompatActivity() {
    private lateinit var gLView: GLSurfaceView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = PencilGLSurfaceView(this@OpenGLES20Activity)
//        gLView = MyGLSurfaceView(this@OpenGLES20Activity)
        setContentView(gLView)
    }
}