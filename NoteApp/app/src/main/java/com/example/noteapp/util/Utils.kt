package com.example.noteapp.util

fun FloatArray.print(msg: String) {
    DEVLogger.d("$msg  ${this.contentToString()}")
}