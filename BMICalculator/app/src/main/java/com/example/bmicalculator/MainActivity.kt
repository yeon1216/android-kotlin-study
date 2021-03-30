package com.example.bmicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val heightEt: EditText = findViewById(R.id.heightEt)
        val weightEt = findViewById<EditText>(R.id.weightEt)
        val resultBtn = findViewById<Button>(R.id.resultBtn)

        resultBtn.setOnClickListener {
            if(heightEt.text.isEmpty() || weightEt.text.isEmpty()){
                Toast.makeText(applicationContext,"빈 값이 있습니다.",Toast.LENGTH_SHORT).show()
                // return에 @setOnClickListener가 붙는 이유 : onCreate()의 return인지 setOnClickListener()의 return인지 알려주기 위해
                return@setOnClickListener
            }
        }


    }
}