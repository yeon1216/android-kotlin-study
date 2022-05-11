package com.example.noteapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.example.noteapp.util.DEVLogger
import com.example.noteapp.util.print

class OpenGLViewModel(
    application: Application,
): AndroidViewModel(application) {

    var winWidth = 0
    var winHeight = 0

    private val _lineState: MutableStateFlow<LineState> = MutableStateFlow(LineState.None(true))
    val lineState: StateFlow<LineState> = _lineState
//    private val _lines: MutableStateFlow<MutableList<FloatArray>> = MutableStateFlow(mutableListOf())
//    val lines: StateFlow<List<FloatArray>> = _lines

    var a = 0

    fun updateLines(tempPoint: FloatArray, isStart: Boolean) = viewModelScope.launch {
        val lines = lineState.value.lines as MutableList
        if (isStart) {
            DEVLogger.d("updateLines $isStart")
            logArr("tempPoint", tempPoint)
            for(line: FloatArray in lines)
                line.print("$a line")
            a ++
            lines.add(convertCoordinate(tempPoint))
            _lineState.value = LineState.NewLine(lines)
        } else {
            var tempLine = lines[lines.size - 1]
            tempLine = tempLine.plus(convertCoordinate(tempPoint))
            lines[lines.size - 1] = tempLine
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

sealed class LineState(open val lines: List<FloatArray>) {
    data class NewLine(override val lines: List<FloatArray>): LineState(lines)
    data class Line(override val lines: List<FloatArray>): LineState(lines)
    data class None(val isFirst: Boolean): LineState(lines = mutableListOf())
}
