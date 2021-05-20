package com.example.secretdiary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private val diaryEditText: EditText by lazy {
        findViewById<EditText>(R.id.diaryEditText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        initDetailEditText()

    }

    private fun initDetailEditText() {
        val detail = getSharedPreferences("diary", Context.MODE_PRIVATE).getString("detail", "")
        diaryEditText.setText(detail)

        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit(true) {
                putString("detail", diaryEditText.text.toString())
            }
        }

        // 텍스트 입력이 0.5초 이상 일어나지 않는 경우 쉐어드에 입력 내용이 저장되도록 구현
        diaryEditText.addTextChangedListener {
            Log.d("DiaryActivity", "text Changed :: $it")
            //
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500)
        }
    }

}