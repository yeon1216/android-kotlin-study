package com.example.noteapp.util

import android.util.Log
import com.example.noteapp.BuildConfig
import java.lang.Exception

object DEVLogger {

    private const val isDevMode: Boolean = false
    var isDebug: Boolean = BuildConfig.DEBUG

    fun d(msg: String) {
        if (isShowLog())
            Log.d(buildLogTag(), msg)
    }

    fun d(tag: String, msg: String) {
        if (isShowLog())
            Log.d(tag, msg)
    }

    fun e(msg: String) {
        if (isShowLog())
            Log.e(buildLogTag(), msg)
    }

    fun e(msg: String, exception: Exception) {
        if (isShowLog())
            Log.e(buildLogTag(), msg, exception)
    }

    fun e(tag: String, msg: String) {
        if (isShowLog())
            Log.e(tag, msg)
    }

    fun e(tag: String, msg: String, exception: Exception) {
        if (isShowLog())
            Log.e(tag, msg)
    }

    private fun isShowLog(): Boolean {
        return if (isDebug) {
            true
        } else isDevMode
    }

    private fun buildLogTag() : String {
        val ste = Thread.currentThread().stackTrace[4]
        val sb = StringBuilder()
        sb.append("YEON  [")
        sb.append(ste.fileName.replace(".java", "", false))
        sb.append("::")
        sb.append(ste.methodName)
        sb.append("]")
        return sb.toString()
    }

}