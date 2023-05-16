package com.inf151313.boardgamecollector

import adapter.BoardGameAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import database.BoardGameDataSource


class BoardGameListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var boardGameAdapter: BoardGameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_game_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dataSource = BoardGameDataSource(this)
        val boardGames = dataSource.getAllBoardGames()

        boardGameAdapter = BoardGameAdapter(this, boardGames)
        recyclerView.adapter = boardGameAdapter
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}

