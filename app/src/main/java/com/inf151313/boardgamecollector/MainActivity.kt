package com.inf151313.boardgamecollector


import model.XmlParserTask
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val cache = getSharedPreferences("cache", Context.MODE_PRIVATE)

        if(!cache.getBoolean("firstSync", false)) {
            val helloTextView = findViewById<TextView>(R.id.helloText2)
            helloTextView.text = "Hello, " + cache.getString("username", "")

            val expansionNumber: TextView = findViewById(R.id.textExtensionNumber)
            var url = "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString("username", "") +
                    "&subtype=boardgameexpansion"
            var parserTask = XmlParserTask().execute(url)
            val expansionItemCount = parserTask.get().size
            expansionNumber.text = "You have " + expansionItemCount + " expansions!"

            val gamesNumber: TextView = findViewById(R.id.textGameNumber)
            url = "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString("username", "") +
                    "&subtype=boardgame&excludesubtype=boardgameexpansion"
            parserTask = XmlParserTask().execute(url)
            var gameItemCount = parserTask.get().size
            gamesNumber.text = "You have " + gameItemCount + " games!"

            val currentDate = LocalDate.now()
            val dateString = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString()
            val lastSyncDate: TextView = findViewById(R.id.textLastSynchronised)
            lastSyncDate.text = "Last synchronised: " + cache.getString("lastSync", dateString)

            cache.edit().putString("syncDate", currentDate.format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString()).apply()
            cache.edit().putBoolean("firstSync", true).apply()
        } else {

        }
    }



    fun clearDataClick (v: View) {
        clearData()
    }
    private fun clearData () {
        val cache = getSharedPreferences("cache", Context.MODE_PRIVATE)
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Clear data?")
            .setCancelable(false)
            .setPositiveButton("yes") { _, _ ->
                cache.edit().putBoolean("firstSync", false).apply()
                cache.edit().putBoolean("configDone", false).apply()
                Toast.makeText(this, "Application will restart soon!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ConfigActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

}


