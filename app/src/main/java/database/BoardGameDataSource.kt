package database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import model.BoardGame
import model.Image
import model.ImageFile


class BoardGameDataSource(context: Context) {

    private val dbHelper: DatabaseHelper = DatabaseHelper(context)
    private val db: SQLiteDatabase = dbHelper.writableDatabase
    fun close() {
        dbHelper.close()
    }
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
    fun addImageFile(image: ImageFile) {
        val values = ContentValues()
        values.put("game_id", image.gameId)
        values.put("expansion_id", image.expansionId)
        values.put("image_path", image.imagePath)
        db.insert("image_file", null, values)
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
    fun deleteAllImageFiles() {
        db.delete("image_file", null, null)
    }
    fun deleteImageFileByImagePath(imagePath: String) {
        db.delete("image_file", "image_path = ?", arrayOf(imagePath.toString()))
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
    fun getImageFilesByGameId(gameId: Int): List<String> {
        val imageFiles = mutableListOf<String>()

        val query = "SELECT image_path FROM image_file WHERE game_id = ?"
        val cursor = db.rawQuery(query, arrayOf(gameId.toString()))

        while (cursor.moveToNext()) {
            val imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"))
            imageFiles.add(imagePath)
        }
        cursor.close()
        return imageFiles
    }
    @SuppressLint("Range")
    fun getImageFilesByExpansionId(gameId: Int): List<String> {
        val imageFiles = mutableListOf<String>()

        val query = "SELECT image_path FROM image_file WHERE expansion_id = ?"
        val cursor = db.rawQuery(query, arrayOf(gameId.toString()))

        while (cursor.moveToNext()) {
            val imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"))
            imageFiles.add(imagePath)
        }
        cursor.close()
        return imageFiles
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
    fun getExpansionByExpansionId(gameId: Int): Int? {
        val query = "SELECT bgg_id FROM expansion WHERE id = ?"
        val selectionArgs = arrayOf(gameId.toString())
        val cursor = db.rawQuery(query, selectionArgs)

        var expansionBggId: Int? = null
        if (cursor.moveToFirst()) {
            expansionBggId = cursor.getInt(cursor.getColumnIndex("bgg_id"))
        }
        cursor.close()
        return expansionBggId

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