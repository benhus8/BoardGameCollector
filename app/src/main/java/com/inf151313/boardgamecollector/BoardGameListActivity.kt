package com.inf151313.boardgamecollector

import adapter.BoardGameAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import database.BoardGameDataSource
import enums.Type


class BoardGameListActivity : AppCompatActivity(), BoardGameAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var boardGameAdapter: BoardGameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_game_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dataSource = BoardGameDataSource(this)
        val boardGames = dataSource.getAllBoardGames()

        boardGameAdapter = BoardGameAdapter(this, boardGames, Type.BOARDGAME)
        boardGameAdapter.setOnItemClickListener(this)
        recyclerView.adapter = boardGameAdapter
    }
    override fun onItemClick(boardGameId: Int) {
        // Otwórz nowe Activity z przekazanym ID obiektu
        val intent = Intent(this, BoardGameDetailsActivity::class.java)
        intent.putExtra("boardGameId", boardGameId)
        startActivity(intent)
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}

