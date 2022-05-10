package com.example.noteapp.view_model

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.successOr
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.model.Notes
import com.example.noteapp.domain.model.Notes.Companion.toNotes
import com.example.noteapp.domain.repository.NoteRepository
import com.example.noteapp.util.ErrorMessage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import com.example.noteapp.data.Result
import com.example.noteapp.util.DEVLogger
import kotlin.math.sqrt

class OpenGLViewModel(
    application: Application,
): AndroidViewModel(application) {

    var winWidth = 0
    var winHeight = 0

    val coordinates: MutableStateFlow<FloatArray> = MutableStateFlow(floatArrayOf())

    fun updateCoordinates(coordinates: FloatArray) {
        var tempCoordinates = this.coordinates.value
        tempCoordinates = tempCoordinates.plus(convertCoordinate(coordinates))
        this.coordinates.value = tempCoordinates
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