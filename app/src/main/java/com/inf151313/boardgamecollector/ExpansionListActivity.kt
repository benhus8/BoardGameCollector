package com.inf151313.boardgamecollector

import adapter.BoardGameAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import database.BoardGameDataSource
import enums.Type


class ExpansionListActivity : AppCompatActivity(), BoardGameAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var boardGameAdapter: BoardGameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expansion_game_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dataSource = BoardGameDataSource(this)
        val expansions = dataSource.getAllExpansions()

        boardGameAdapter = BoardGameAdapter(this, expansions, Type.EXPANSION)
        boardGameAdapter.setOnItemClickListener(this)
        recyclerView.adapter = boardGameAdapter
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    override fun onItemClick(expansionId: Int) {
        // Otwórz nowe Activity z przekazanym ID obiektu
        val intent = Intent(this, ExpansionDetailsActivity::class.java)
        intent.putExtra("expansionId", expansionId)
        startActivity(intent)
    }

}

