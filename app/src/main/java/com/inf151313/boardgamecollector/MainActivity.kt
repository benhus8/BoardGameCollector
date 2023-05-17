package com.inf151313.boardgamecollector


import XmlParserTask

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import database.BoardGameDataSource
import model.BoardGame
import model.Image
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("WE WERE HERE", "TEST_APP")
        val cache = getSharedPreferences("cache", Context.MODE_PRIVATE)
        performRoutineSetup(cache)
    }
    override fun onResume() {
        super.onResume()
        val cache = getSharedPreferences("cache", Context.MODE_PRIVATE)
            performRoutineSetup(cache)
        }

    fun clearDataClick (v: View) {
        clearData()
    }
    fun mainSynchroniseClick (v: View) {
        moveToSynchroniseData()
    }
    fun mainMyGamesClick (v: View) {
        moveToGameList()
    }
    fun mainExpansionsClick (v: View) {
        moveToExpansionList()
    }
    private fun moveToExpansionList () {
        val intent = Intent(this, ExpansionListActivity::class.java)
        startActivity(intent)
    }
    private fun moveToGameList () {
        val intent = Intent(this, BoardGameListActivity::class.java)
        startActivity(intent)
    }
    private fun moveToSynchroniseData () {
        val intent = Intent(this, SynchroniseActivity::class.java)
        startActivity(intent)
    }
    private fun clearData () {
        val cache = getSharedPreferences("cache", Context.MODE_PRIVATE)
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Clear data?")
            .setCancelable(false)
            .setPositiveButton("yes") { _, _ ->
                cache.edit().putBoolean("firstSync", false).apply()
                cache.edit().putBoolean("configDone", false).apply()
                Toast.makeText(this, "Application restarted!", Toast.LENGTH_SHORT).show()
                val dataSource = BoardGameDataSource(this)
                dataSource.deleteAllExpansions()
                dataSource.deleteAllBoardGames()
                dataSource.deleteAllImages()
                dataSource.deleteAllImageFiles()
                val intent = Intent(this, ConfigActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun performRoutineSetup(cache: SharedPreferences) {

        val dataSource = BoardGameDataSource(this)

        val helloTextView = findViewById<TextView>(R.id.helloText2)
        helloTextView.text = "Hello, " + cache.getString("username", "")

        val expansionNumber: TextView = findViewById(R.id.textExtensionNumber)
        expansionNumber.text = "You have " + dataSource.getAllExpansions().size + " expansions!"

        val gamesNumber: TextView = findViewById(R.id.textLastSynchronised1)
        gamesNumber.text = "You have " + dataSource.getAllBoardGames().size + " games!"

        val lastSyncDate: TextView = findViewById(R.id.textLastSynchronised)
        lastSyncDate.text = "Last synchronised: " + cache.getString("syncDate", "")
    }

}


