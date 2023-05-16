package com.inf151313.boardgamecollector


import XmlParserTask
import adapter.BoardGameAdapter
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
import kotlin.random.Random


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
            var expansions = XmlParserTask().execute(url)
            val expansionItemCount = expansions.get().size
            expansionNumber.text = "You have " + expansionItemCount + " expansions!"

            val gamesNumber: TextView = findViewById(R.id.textLastSynchronised1)
            url = "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString("username", "") +
                    "&subtype=boardgame&excludesubtype=boardgameexpansion"
            var boardgames = XmlParserTask().execute(url)
            var gameItemCount = boardgames.get().size
            gamesNumber.text = "You have " + gameItemCount + " games!"

            val currentDate = LocalDate.now()
            val dateString = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString()

            val lastSyncDate: TextView = findViewById(R.id.textLastSynchronised)
            cache.edit().putString("lastSync", dateString).apply()
            lastSyncDate.text = "Last synchronised: " + cache.getString("lastSync", dateString)

            val dataSource = BoardGameDataSource(this)

            for (expansion in expansions.get()) {
                val newExpansionId = dataSource.addBoardExtension(
                    BoardGame(
                        id = 0,
                        title = expansion.title,
                        originalTitle = expansion.title,
                        yearPublished = expansion.yearPublished,
                        bggId = expansion.bggId
                    )
                )
                dataSource.addImage(
                    Image(
                        expansionId = newExpansionId.toInt(),
                        gameId = null,
                        imagePath = expansion.image,
                        thumbnail = expansion.thumbnail)
                )
                Log.d("TEST_DATABASE", newExpansionId.toString())
            }

            for (boardGame in boardgames.get()) {
                val newBoardGameId =  dataSource.addBoardGame(
                    BoardGame(
                        id = 0,
                        title = boardGame.title,
                        originalTitle = boardGame.title,
                        yearPublished = boardGame.yearPublished,
                        bggId = boardGame.bggId)
                )
                Log.d("adding to database", boardGame.thumbnail)
                dataSource.addImage(
                    Image(
                        expansionId = null,
                        gameId = newBoardGameId.toInt(),
                        imagePath = boardGame.image,
                        thumbnail = boardGame.thumbnail)
                )
            }

            cache.edit().putString("syncDate", currentDate.format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString()).apply()
            cache.edit().putBoolean("firstSync", true).apply()
            cache.edit().putLong("syncDateLong", Instant.now().toEpochMilli()).apply()

        } else {
            performRoutineSetup(cache)

        }
    }
    override fun onResume() {
        super.onResume()
        val cache = getSharedPreferences("cache", Context.MODE_PRIVATE)
        if (cache.getBoolean("firstSync", false)) {
            performRoutineSetup(cache)
        }
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


