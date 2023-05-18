package com.inf151313.boardgamecollector

import XmlParserTask
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import database.BoardGameDataSource
import model.BoardGame
import model.Image
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
                val dataSource = BoardGameDataSource(this)
                dataSource.deleteAllBoardGames()
                dataSource.deleteAllExpansions()
                dataSource.deleteAllImages()
                dataSource.deleteAllImageFiles()
                var url = "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString(
                    "username",
                    ""
                ) +
                        "&subtype=boardgame&excludesubtype=boardgameexpansion"
                val boardgames = XmlParserTask().execute(url)
                url = "https://boardgamegeek.com/xmlapi2/collection?username=" + cache.getString(
                    "username",
                    ""
                ) +
                        "&subtype=boardgameexpansion"
                val expansions = XmlParserTask().execute(url)
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
                    dataSource.addImage(
                        Image(
                            expansionId = null,
                            gameId = newBoardGameId.toInt(),
                            imagePath = boardGame.image,
                            thumbnail = boardGame.thumbnail)
                    )
                }
                dataSource.close()
                val currentDate = LocalDate.now()
                val dateString = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString()
                cache.edit().putString("syncDate", currentDate.format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString()).apply()
                cache.edit().putBoolean("firstSync", true).apply()
                cache.edit().putLong("syncDateLong", Instant.now().toEpochMilli()).apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }


    }

}
