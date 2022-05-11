package com.example.noteapp.ui.gl

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.example.noteapp.view_model.LineState
import com.example.noteapp.view_model.OpenGLViewModel

class PencilGLRenderer : GLSurfaceView.Renderer {

    private lateinit var mPencil: Pencil
    private var viewModel: OpenGLViewModel? = null
    fun setViewModel(viewModel: OpenGLViewModel) {
        this.viewModel = viewModel
        viewModel.setWH(winWidth, winHeight)
    }

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)
    private var winWidth = 0
    private var winHeight = 0




    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // initialize a pencil
        mPencil = Pencil()

        // Set the background frame color
        GLES20.glClearColor(0.1f, 0.5f, 0.5f, 1.0f)
    }

    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        viewModel?.lineState?.let { lineState ->
            val tempLineState = lineState.value
            val lines = tempLineState.lines
            when (tempLineState) {
                is LineState.NewLine -> {
                    mPencil.draw(lines, vPMatrix, GLES20.GL_LINES)
                }
                is LineState.Line -> {
                    mPencil.draw(lines, vPMatrix, GLES20.GL_LINE_STRIP)
                }
            }
        }

    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        viewModel?.setWH(height = height, width = width)
        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

}
