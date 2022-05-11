package com.example.noteapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.*
import com.example.noteapp.util.DEVLogger

class OpenGLViewModel(
    application: Application,
): AndroidViewModel(application) {

    var winWidth = 0
    var winHeight = 0

    private val _lineState: MutableStateFlow<LineState> = MutableStateFlow(LineState.None(true))
    val lineState: StateFlow<LineState> = _lineState

    fun updateLines(tempPoint: FloatArray, isNewLine: Boolean) {
        var lines = lineState.value.lines
        lines = lines.plus(convertCoordinate(tempPoint))
        if (isNewLine) {
            _lineState.value = LineState.NewLine(lines)
        } else {
            _lineState.value = LineState.Line(lines)
        }
    }

    fun setWH(width: Int, height: Int) {
        DEVLogger.d("setWH() width : $width, height: $height")
        winHeight = height
        winWidth = width
    }

    private fun convertCoordinate(winCoordinate: FloatArray): FloatArray {
        val w = winWidth
        val h = winHeight
        winCoordinate[0] = -((winCoordinate[0] - (w.toFloat() / 2.0F)) * (1.0F / (w / 2.0F)))
        winCoordinate[1] = -((winCoordinate[1] - (h.toFloat() / 2.0F)) * (1.0F / (h / 2.0F)))
        return winCoordinate
    }

    private fun logArr(msg: String, arr: FloatArray) {
        arr.forEachIndexed { index, fl ->
            val i = index / 3
            val j = index % 3
            DEVLogger.d("$msg ($i, $j) : $fl")
        }
    }

}

sealed class LineState(open val lines: FloatArray) {
    data class NewLine(override val lines: FloatArray): LineState(lines = lines)
    data class Line(override val lines: FloatArray): LineState(lines = lines)
    data class None(val isFirst: Boolean): LineState(lines = floatArrayOf())
}