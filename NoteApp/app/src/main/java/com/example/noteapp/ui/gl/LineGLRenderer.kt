package com.example.noteapp.ui.gl

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.example.noteapp.util.DEVLogger
import com.example.noteapp.view_model.LineState
import com.example.noteapp.view_model.OpenGLViewModel
import kotlin.math.sqrt

class LineGLRenderer : GLSurfaceView.Renderer {

    private lateinit var mLine: Line
    private var viewModel: OpenGLViewModel? = null
    fun setViewModel(viewModel: OpenGLViewModel) {
        this.viewModel = viewModel
        DEVLogger.d("setViewModel() ")
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
        mLine = Line()

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

//        viewModel?.lines?.let { mPencil.draw(it.value, vPMatrix) }

        viewModel?.lineState?.let { lineState ->
            val tempLineState = lineState.value
            val lines = tempLineState.lines
            when (tempLineState) {
                is LineState.Line -> {
                    mLine.draw(lines[lines.size - 1], vPMatrix)
                }
                is LineState.NewLine -> {
//                    mLine = Line()
                    mLine.draw(lines[lines.size - 1], vPMatrix)
//                    isNewLine = false
                }
            }
        }

//        viewModel?.lines?.let { lines ->
//            DEVLogger.d("viewModel?.lines?.let")
//            if (isNewLine && (lines.value.size >= 1)) {
//                DEVLogger.d("viewModel?.lines?.let 1")
//                mLine = Line()
//                mLine.draw(lines.value[lines.value.size - 1], vPMatrix)
//                isNewLine = false
//            }
//            if (!isNewLine) {
//                DEVLogger.d("viewModel?.lines?.let 2")
//                mLine.draw(lines.value[lines.value.size - 1], vPMatrix)
//            }
//        }
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        viewModel?.setWH(height = height, width = width)
        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    var isNewLine = true

    fun setIsNewLine() {
        isNewLine = true
    }

}
