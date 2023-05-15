package com.inf151313.boardgamecollector


import XmlParserTask
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import database.BoardGameDataSource
import model.BoardGame
import model.Image
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random


class SynchroniseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_synchronise)

        val cache = getSharedPreferences("cache", MODE_PRIVATE)
        val lastSyncDate: TextView = findViewById(R.id.textLastSynchronised1)
        lastSyncDate.text = "Last synchronised: " + cache.getString("syncDate", "")

    }
    fun onSyncClick(v: View) {
        synchronization()
    }

    private fun synchronization() {
        val cache = getSharedPreferences("cache", MODE_PRIVATE)
        val lastSyncDate: TextView = findViewById(R.id.textLastSynchronised1)

        val syncText: TextView = findViewById(R.id.textSyncProgress)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        progressBar.max = 100
        val lastSyncLong = cache.getLong("syncDateLong", Instant.now().toEpochMilli())
        //we ca synchronise data
        if(
//            lastSyncLong < Instant.now().minusSeconds(86400).toEpochMilli()
            true
        ) {
            Thread(Runnable {
                syncText.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
                //boardgames
                var url = "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString(
                    "username",
                    ""
                ) +
                        "&subtype=boardgame&excludesubtype=boardgameexpansion"
                val boardgames = XmlParserTask().execute(url)
                runOnUiThread {
                    val number = Random.nextInt(0, 28)
                    progressBar.setProgress(number, true)
                    syncText.text = "Synchronization Progress: " + number + "%"
                }
                Thread.sleep(250)
                //expansions
                url = "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString(
                    "username",
                    ""
                ) +
                        "&subtype=boardgameexpansion"
                val expansions = XmlParserTask().execute(url)
                runOnUiThread {
                    val number = Random.nextInt(30, 47)
                    progressBar.setProgress(number, true)
                    syncText.text = "Synchronization Progress: " + number + "%"
                }
                Thread.sleep(250)

                val dataSource = BoardGameDataSource(this)
                dataSource.deleteAllBoardGames()
                dataSource.deleteAllExpansions()
                dataSource.deleteAllImages()
                runOnUiThread {
                    val number = Random.nextInt(50, 60)
                    progressBar.setProgress(number, true)
                    syncText.text = "Synchronization Progress: " + number + "%"
                }
                Thread.sleep(250)

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
                runOnUiThread {
                    val number = Random.nextInt(70, 80)
                    progressBar.setProgress(number, true)
                    syncText.text = "Synchronization Progress: " + number + "%"
                }
                Thread.sleep(250)

                for (boardGame in boardgames.get()) {
                    val newBoardGameId =  dataSource.addBoardGame(
                        BoardGame(
                            id = 0,
                            title = boardGame.title,
                            originalTitle = boardGame.title,
                            yearPublished = boardGame.yearPublished,
                            bggId = boardGame.bggId))

                    dataSource.addImage(
                        Image(
                            expansionId = newBoardGameId.toInt(),
                            gameId = null,
                            imagePath = boardGame.image,
                            thumbnail = boardGame.thumbnail))
                }

                runOnUiThread {
                    progressBar.setProgress(100, true)
                    syncText.text = "Synchronization Progress: " + 100 + "%"
                    syncText.visibility = View.GONE
                    progressBar.visibility = View.GONE

                    val currentDate = LocalDate.now()
                    cache.edit().putString(
                        "syncDate", currentDate.format(
                            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        ).toString()
                    ).apply()
                    lastSyncDate.text = "Last synchronised: " + cache.getString("syncDate", "")
                    cache.edit().putLong("syncDateLong", Instant.now().toEpochMilli()).apply()
                    Toast.makeText(this, "Synchronization completed!", Toast.LENGTH_SHORT).show()
                }
            }).start()
        } else {
            Toast.makeText(this, "Data is already synchronised!", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}