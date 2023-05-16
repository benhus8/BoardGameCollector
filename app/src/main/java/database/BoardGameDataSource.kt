package database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import model.BoardGame
import model.Image


class BoardGameDataSource(context: Context) {

    private val dbHelper: DatabaseHelper = DatabaseHelper(context)
    private val db: SQLiteDatabase = dbHelper.writableDatabase

    fun addBoardGame(boardGame: BoardGame): Long {
        val values = ContentValues()
        values.put("title", boardGame.title)
        values.put("original_title", boardGame.originalTitle)
        values.put("year_published", boardGame.yearPublished)
        values.put("bgg_id", boardGame.bggId)
        return db.insert("boardgame", null, values)
    }
    fun addBoardExtension(boardGame: BoardGame): Long {
        val values = ContentValues()
        values.put("title", boardGame.title)
        values.put("original_title", boardGame.originalTitle)
        values.put("year_published", boardGame.yearPublished)
        values.put("bgg_id", boardGame.bggId)
        return  db.insert("expansion", null, values)
    }
    fun addImage(image: Image) {
        val values = ContentValues()
        values.put("game_id", image.gameId)
        values.put("expansion_id", image.expansionId)
        values.put("image_path", image.imagePath)
        values.put("thumbnail", image.thumbnail)
        db.insert("image", null, values)
    }

    @SuppressLint("Range")
    fun getAllBoardGames(): List<BoardGame> {
        val boardGames = mutableListOf<BoardGame>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM boardgame", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val title = cursor.getString(cursor.getColumnIndex("title"))
            val originalTitle = cursor.getString(cursor.getColumnIndex("original_title"))
            val yearPublished = cursor.getInt(cursor.getColumnIndex("year_published"))
            val bggId = cursor.getInt(cursor.getColumnIndex("bgg_id"))
            val boardGame = BoardGame(id, title, originalTitle, yearPublished, bggId)
            boardGames.add(boardGame)
        }
        cursor.close()
        return boardGames
    }
    fun deleteAllBoardGames() {
        db.delete("boardgame", null, null)
    }
    fun deleteAllExpansions() {
        db.delete("expansion", null, null)
    }
    fun deleteAllImages() {
        db.delete("image", null, null)
    }

    @SuppressLint("Range")
    fun getAllExpansions(): List<BoardGame> {
        val expansions = mutableListOf<BoardGame>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM expansion", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val title = cursor.getString(cursor.getColumnIndex("title"))
            val originalTitle = cursor.getString(cursor.getColumnIndex("original_title"))
            val yearPublished = cursor.getInt(cursor.getColumnIndex("year_published"))
            val bggId = cursor.getInt(cursor.getColumnIndex("bgg_id"))
            val boardGame = BoardGame(id, title, originalTitle, yearPublished, bggId)
            expansions.add(boardGame)
        }
        cursor.close()
        return expansions
    }
    @SuppressLint("Range")
    fun getThumbnailByGameId(gameId: Int): String? {
        val query = "SELECT thumbnail FROM image WHERE game_id = ?"
        val selectionArgs = arrayOf(gameId.toString())
        val cursor = db.rawQuery(query, selectionArgs)

        var thumbnail: String? = null
        if (cursor.moveToFirst()) {
            thumbnail = cursor.getString(cursor.getColumnIndex("thumbnail"))
        }
        cursor.close()
        return thumbnail
    }
    @SuppressLint("Range")
    fun getBoardGameByGameId(gameId: Int): Int? {
        val query = "SELECT bgg_id FROM boardgame WHERE id = ?"
        val selectionArgs = arrayOf(gameId.toString())
        val cursor = db.rawQuery(query, selectionArgs)

        var boardGameBggId: Int? = null
        if (cursor.moveToFirst()) {
            boardGameBggId = cursor.getInt(cursor.getColumnIndex("bgg_id"))
        }
        cursor.close()
        return boardGameBggId
    }
    @SuppressLint("Range")
    fun getThumbnailByExpansionId(gameId: Int): String? {
        val query = "SELECT thumbnail FROM image WHERE expansion_id = ?"
        val selectionArgs = arrayOf(gameId.toString())
        val cursor = db.rawQuery(query, selectionArgs)

        var thumbnail: String? = null
        if (cursor.moveToFirst()) {
            thumbnail = cursor.getString(cursor.getColumnIndex("thumbnail"))
        }
        cursor.close()
        return thumbnail
    }

}