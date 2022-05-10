package com.example.noteapp.ui.gl

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.example.noteapp.util.DEVLogger
import kotlin.math.sqrt

class PencilGLRenderer : GLSurfaceView.Renderer {

    private lateinit var mPencil: Pencil

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)
    private var winWidth = 0
    private var winHeight = 0

    @Volatile
    var coordi: FloatArray = FloatArray(6)

    private fun logArr(msg: String, arr: FloatArray) {
        arr.forEachIndexed { index, fl ->
            val i = index / sqrt(arr.size.toDouble()).toInt()
            val j = index % sqrt(arr.size.toDouble()).toInt()
            DEVLogger.d("$msg ($i, $j) : $fl")
        }
    }


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

        mPencil.draw(convertCoordinate(coordi), vPMatrix)
    }

    private fun convertCoordinate(winCoordinate: FloatArray): FloatArray {
        val w = winWidth
        val h = winHeight
        winCoordinate[0] = -((winCoordinate[0] - (w.toFloat() / 2.0F)) * (1.0F / (w / 2.0F)))
        winCoordinate[1] = -((winCoordinate[1] - (h.toFloat() / 2.0F)) * (1.0F / (h / 2.0F)))
        winCoordinate[3] = -((winCoordinate[3] - (w.toFloat() / 2.0F)) * (1.0F / (w / 2.0F)))
        winCoordinate[4] = -((winCoordinate[4] - (h.toFloat() / 2.0F)) * (1.0F / (h / 2.0F)))
        // logArr("winCoordinate", winCoordinate)
        return winCoordinate
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        winHeight = height
        winWidth = width
        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

}
