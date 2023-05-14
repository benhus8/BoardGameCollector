package com.inf151313.boardgamecollector

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class ConfigActivity : AppCompatActivity() {

    private var inputUsername: EditText? = null
    private lateinit var acceptUsername: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cache = getSharedPreferences("cache", Context.MODE_PRIVATE)

        if(cache.getBoolean("configDone", false)) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        } else {
            setContentView(R.layout.activity_config)
            inputUsername = findViewById(R.id.inputUsername)
            acceptUsername = findViewById(R.id.acceptUsername)

            acceptUsername.setOnClickListener {
                val editText = findViewById<EditText>(R.id.inputUsername)
                val text = editText.text.toString()
                cache.edit().putString("username", text).apply()
                Log.d("MainActivity", text)
                cache.edit().putBoolean("configDone", true).apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }


    }

}
