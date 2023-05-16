package com.inf151313.boardgamecollector

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import database.BoardGameDataSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.BoardGameDetailsParser


class BoardGameDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_game_details)

        val boardGameId = intent.getIntExtra("boardGameId", 0)

        val dataSource = BoardGameDataSource(this)
        val boardGameBggId = dataSource.getBoardGameByGameId(boardGameId)

        val detailsTitle = findViewById<TextView>(R.id.textDetailsTitle)
        detailsTitle.text = "Game Bgg id:" + boardGameBggId.toString()

        val xmlUrl = "https://boardgamegeek.com/xmlapi2/thing?id=102794&stats=1"
        val xmlParser = BoardGameDetailsParser(boardGameBggId.toString())

        GlobalScope.launch {
            val gameDetails = xmlParser.parse()
            runOnUiThread {
                //modify UI HERE
                detailsTitle.text = "Game Bgg id:" + gameDetails.rank

            }
        }

    }
}