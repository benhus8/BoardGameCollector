package com.inf151313.boardgamecollector

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val cache = getSharedPreferences("cache", Context.MODE_PRIVATE)
        val helloTextView = findViewById<TextView>(R.id.helloText)
        helloTextView.text = "Hello, " + cache.getString("username", "")

    }


}


