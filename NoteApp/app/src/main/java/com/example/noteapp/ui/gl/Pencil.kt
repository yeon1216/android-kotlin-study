package com.example.noteapp.ui.gl

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

// number of coordinates per vertex in this array
const val COORDINATES_PER_VERTEX_PENCIL = 3

var pencilCoordinatesSize = 6

class Pencil {

    private val vertexShaderCode =
        // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() {" +
            // the matrix must be included as a modifier of gl_Position
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
        "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    private var mProgram: Int

    // Use to access and set the view transformation
    private var vPMatrixHandle: Int = 0
    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = pencilCoordinatesSize / COORDINATES_PER_VERTEX_PENCIL
    private val vertexStride: Int = COORDINATES_PER_VERTEX_PENCIL * 4 // 4 bytes per vertex

    init {

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program®
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }
    }


    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    fun draw(pencilCoords: FloatArray, mvpMatrix: FloatArray) {
        GLES20.glUseProgram(mProgram)
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            val vertexBuffer: FloatBuffer =
            ByteBuffer.allocateDirect(pencilCoordinatesSize * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(pencilCoords)
                    position(0)
                }
            }

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                it,
                COORDINATES_PER_VERTEX_PENCIL,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle
                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)
        }

        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

        // Draw the line
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount)

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle)
    }

}
