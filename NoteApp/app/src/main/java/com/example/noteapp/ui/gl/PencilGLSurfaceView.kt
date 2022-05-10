package com.example.noteapp.ui.gl

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

class PencilGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: PencilGLRenderer

    init {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)
        renderer = PencilGLRenderer()
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
        // Render the view only when there is a change in the drawing data.
        // To allow the triangle to rotate automatically, this line is commented out:
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        val x: Float = e.x
        val y: Float = e.y

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                renderer.coordi = floatArrayOf(
                    previousX , previousY, 0f,
                    x, y, 0f
                )

                requestRender()
            }
        }

        previousX = x
        previousY = y
        return true
    }

}
